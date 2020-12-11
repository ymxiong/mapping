package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:30:32
 */
public class BuildMapStaticPipeline extends BasePipeline {

    private MethodSpec.Builder buildMapStaticMethodSpec;

    public BuildMapStaticPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());

        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // init: build map
        logger.debug("Mapping build init buildMap:" + type.getQualifiedName());
        String buildMapStaticMethod = "buildMap";
        buildMapStaticMethodSpec = MethodSpec.methodBuilder(buildMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(ClassUtils.getParameterizedObjectMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = $T.buildMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = new $T<>()", ClassUtils.getLinkedHashMap());
        }
        buildMapStaticMethodSpec.addStatement("if (obj == null) return map");


        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

        buildMapStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", " + modifyStrategy.getModifyName("obj") + ")");
        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // add return
        buildMapStaticMethodSpec.addStatement("return map");

        typeSpec.addMethod(buildMapStaticMethodSpec.build());

        return typeSpec;
    }
}
