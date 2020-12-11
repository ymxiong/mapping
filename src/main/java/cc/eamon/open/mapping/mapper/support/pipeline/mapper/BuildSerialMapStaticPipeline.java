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
import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:35:59
 */
public class BuildSerialMapStaticPipeline extends BasePipeline {

    private MethodSpec.Builder buildSerialMapStaticMethodSpec;

    public BuildSerialMapStaticPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());

        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());


        // init: build serial map
        logger.debug("Mapping build init buildSerialMap:" + type.getQualifiedName());
        String buildSerialMapStaticMethod = "buildSerialMap";
        buildSerialMapStaticMethodSpec = MethodSpec.methodBuilder(buildSerialMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(ClassUtils.getParameterizedStringMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildSerialMapStaticMethodSpec.addStatement("Map<String, String> map = $T.buildSerialMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildSerialMapStaticMethodSpec.addStatement("Map<String, String> map = new $T<>()", ClassUtils.getLinkedHashMap());
        }

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());


        buildSerialMapStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", $T.toJSONString(" + modifyStrategy.getModifyName("obj") + "))", JSONObject.class);

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // add return
        buildSerialMapStaticMethodSpec.addStatement("return map");

        typeSpec.addMethod(buildSerialMapStaticMethodSpec.build());
        return typeSpec;
    }
}
