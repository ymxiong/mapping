package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
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
 * Time: 2020-03-07 17:29:03
 */
public class BuildMapPipeline extends BasePipeline {

    private MethodSpec.Builder buildMapMethodSpec = null;

    public BuildMapPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // init: build self map
        logger.info("Mapping build init buildMap :" + type.getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());


        String buildMapMethod = "buildMap";
        buildMapMethodSpec = MethodSpec.methodBuilder(buildMapMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(ClassUtils.getParameterizedObjectMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildMapMethodSpec.addStatement("Map<String, Object> map = $T.buildMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildMapMethodSpec.addStatement("Map<String, Object> map = new $T<>()", ClassUtils.getLinkedHashMap());
        }

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        buildMapMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\",this." + renameStrategy.getName() + ")");
        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        buildMapMethodSpec.addStatement("return map");
        typeSpec.addMethod(buildMapMethodSpec.build());
        return typeSpec;
    }

}
