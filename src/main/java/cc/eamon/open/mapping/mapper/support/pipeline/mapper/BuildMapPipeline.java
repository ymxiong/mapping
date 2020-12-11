package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraStrategy;
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

    private ExtraStrategy extraStrategy;

    public BuildMapPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // init: build self map
        logger.debug("Mapping build init buildMap :" + type.getQualifiedName());
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

        extraStrategy = (ExtraStrategy) type.getStrategies().get(MapperEnum.EXTRA.getName());

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
        if (extraStrategy.open()) {
            for (MapperField extraField : extraStrategy.getMapperFields()) {
                RenameStrategy extraRenameStrategy = (RenameStrategy) extraField.getStrategies().get(MapperEnum.RENAME.getName());
                buildMapMethodSpec.addStatement("map.put(\"" + extraRenameStrategy.getName() + "\",this." + extraRenameStrategy.getName() + ")");
            }
        }
        buildMapMethodSpec.addStatement("return map");
        typeSpec.addMethod(buildMapMethodSpec.build());
        return typeSpec;
    }

}
