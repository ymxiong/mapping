package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ConstructorIgnoreStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * @author: lucas
 * @date: 2020/7/18
 * @email: lucas@eamon.cc
 */
public class ConstructorPipeline extends BasePipeline {

    private MethodSpec.Builder parameterConstructorMethodSpec;

    private MethodSpec.Builder nonParameterConstructorMethodSpec;

    private boolean hasField = false;

    public ConstructorPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {

        // init: build map
        logger.info("Mapping build init nonParameterConstructorMethodSpec:" + type.getQualifiedName());
        nonParameterConstructorMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        // init: build map
        logger.info("Mapping build init parameterConstructorMethodSpec:" + type.getQualifiedName());
        parameterConstructorMethodSpec = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());
        ConstructorIgnoreStrategy constructorIgnoreStrategy = (ConstructorIgnoreStrategy) field.getStrategies().get(MapperEnum.CONSTRUCTORIGNORE.getName());

        if (!constructorIgnoreStrategy.ignore()) {
            parameterConstructorMethodSpec.addParameter(TypeName.get(modifyStrategy.getModifyType()), renameStrategy.getName());
            parameterConstructorMethodSpec.addStatement("this." + renameStrategy.getName() + "=" + renameStrategy.getName());
            hasField = true;
        }

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {
        typeSpec.addMethod(nonParameterConstructorMethodSpec.build());
        if (hasField) typeSpec.addMethod(parameterConstructorMethodSpec.build());
        return typeSpec;
    }
}
