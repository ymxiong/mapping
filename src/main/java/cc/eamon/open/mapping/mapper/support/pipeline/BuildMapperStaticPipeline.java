package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperStrategy;
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
 * Time: 2020-03-07 17:29:03
 */
public class BuildMapperStaticPipeline extends BasePipeline {

    private MethodSpec.Builder buildMapperStaticMethodSpec = null;

    public BuildMapperStaticPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // mapper strategies
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) type.getStrategies().get(MapperEnum.MAPPER.getName());

        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());
        ClassName selfMapperClassName = ClassName.get(type.getPackageName(), basicMapperStrategy.getBuildTypeName());

        // init: build mapper
        logger.info("Mapping build init buildMapper :" + type.getQualifiedName());
        String buildMapperStaticMethod = "buildMapper";
        buildMapperStaticMethodSpec = MethodSpec.methodBuilder(buildMapperStaticMethod)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(selfMapperClassName);

        buildMapperStaticMethodSpec.addStatement("$T mapper = new $T()", selfMapperClassName, selfMapperClassName);


        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

        buildMapperStaticMethodSpec.addStatement("mapper." + renameStrategy.getName() + "=" + modifyStrategy.getModifyName("obj"));

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        buildMapperStaticMethodSpec.addStatement("return mapper");

        typeSpec.addMethod(buildMapperStaticMethodSpec.build());
        return typeSpec;
    }

}
