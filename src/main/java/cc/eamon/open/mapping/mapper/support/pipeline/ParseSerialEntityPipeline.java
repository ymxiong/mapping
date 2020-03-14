package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class ParseSerialEntityPipeline extends BasePipeline {

    private MethodSpec.Builder parseSerialEntityStaticMethodSpec;

    public ParseSerialEntityPipeline(Pipeline pipeline) {
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
        String parseSerialEntityStaticMethod = "parseSerialEntity";
        parseSerialEntityStaticMethodSpec = MethodSpec.methodBuilder(parseSerialEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassUtils.getParameterizedStringMap(), "map")
                .returns(self);

        // build obj
        parseSerialEntityStaticMethodSpec.addStatement("$T obj = new $T()", self, self);
        if (extendsStrategy.open()) {
            parseSerialEntityStaticMethodSpec.addStatement("$T.copyEntity($T.parseEntity(map), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()), ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
        parseSerialEntityStaticMethodSpec.addStatement("if (map == null) return obj");


        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());


        if (MapperUtils.loadTypeArguments(modifyStrategy.getModifyType()).size() > 0) {
            parseSerialEntityStaticMethodSpec.addStatement(modifyStrategy.getRecoverName("obj").replace("$", "$T.parseObject(map.get(\"" + renameStrategy.getName() + "\"), new $T<$T>(){})"),
                    JSONObject.class, TypeReference.class, ClassName.get(modifyStrategy.getModifyType()));
        } else {
            parseSerialEntityStaticMethodSpec.addStatement(modifyStrategy.getRecoverName("obj").replace("$", "$T.parseObject(map.get(\"" + renameStrategy.getName() + "\"), $T.class)"),
                    JSONObject.class, TypeName.get(modifyStrategy.getModifyType()));
        }
        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // add return
        parseSerialEntityStaticMethodSpec.addStatement("return obj");
        // add method
        typeSpec.addMethod(parseSerialEntityStaticMethodSpec.build());

        return typeSpec;
    }
}
