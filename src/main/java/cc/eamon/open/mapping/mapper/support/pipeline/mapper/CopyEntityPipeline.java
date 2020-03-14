package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class CopyEntityPipeline extends BasePipeline {

    private MethodSpec.Builder copyEntityStaticMethodSpec;

    public CopyEntityPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // init: copy entity
        logger.info("Mapping build init copyEntity:" + type.getQualifiedName());
        String copyEntityStaticMethod = "copyEntity";
        copyEntityStaticMethodSpec = MethodSpec.methodBuilder(copyEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "from")
                .addParameter(self, "to")
                .returns(TypeName.VOID);

        copyEntityStaticMethodSpec.addStatement("if (from == null || to == null) return");
        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        copyEntityStaticMethodSpec.addStatement("to.set" + StringUtils.firstWordToUpperCase(field.getSimpleName()) + "(from.get" + StringUtils.firstWordToUpperCase(field.getSimpleName()) + "())");

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        typeSpec.addMethod(copyEntityStaticMethodSpec.build());
        return typeSpec;
    }
}
