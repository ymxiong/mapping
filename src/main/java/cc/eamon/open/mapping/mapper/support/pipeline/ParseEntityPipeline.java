package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class ParseEntityPipeline extends BasePipeline {

    private MethodSpec.Builder parseEntityStaticMethodSpec;

    public ParseEntityPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());

        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // init: parse entity
        logger.info("Mapping build init parseEntity:" + type.getQualifiedName());
        String parseEntityStaticMethod = "parseEntity";
        parseEntityStaticMethodSpec = MethodSpec.methodBuilder(parseEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassUtils.getParameterizedObjectMap(), "map")
                .returns(self);

        // build obj
        parseEntityStaticMethodSpec.addStatement("$T obj = new $T()", self, self);
        if (extendsStrategy.open()) {
            parseEntityStaticMethodSpec.addStatement("$T.copyEntity($T.parseEntity(map), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()), ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
        parseEntityStaticMethodSpec.addStatement("if (map == null) return obj");


        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

        parseEntityStaticMethodSpec.addStatement(modifyStrategy.getRecoverName("obj").replace("$", "($T) map.get(\"" + renameStrategy.getName() + "\")"),
                TypeName.get(modifyStrategy.getModifyType()));
        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // add return
        parseEntityStaticMethodSpec.addStatement("return obj");

        typeSpec.addMethod(parseEntityStaticMethodSpec.build());
        return typeSpec;
    }
}
