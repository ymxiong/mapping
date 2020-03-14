package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class BuildEntityPipeline extends BasePipeline {

    private MethodSpec.Builder buildEntityMethodSpec;

    public BuildEntityPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());


        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // init: build entity
        logger.info("Mapping build init buildEntity:" + type.getQualifiedName());
        String buildEntityMethod = "buildEntity";
        buildEntityMethodSpec = MethodSpec.methodBuilder(buildEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(self);

        // build obj
        buildEntityMethodSpec.addStatement("$T obj = new $T()", self, self);
        if (extendsStrategy.open()) {
            buildEntityMethodSpec.addStatement("$T.copyEntity(super.buildEntity(), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

        buildEntityMethodSpec.addStatement(modifyStrategy.getRecoverName("obj").replace("$", "this." + renameStrategy.getName()));

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // add return
        buildEntityMethodSpec.addStatement("return obj");

        typeSpec.addMethod(buildEntityMethodSpec.build());
        return typeSpec;
    }
}
