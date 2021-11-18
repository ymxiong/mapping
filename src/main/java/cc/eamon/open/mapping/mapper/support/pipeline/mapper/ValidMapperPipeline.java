package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.enhancement.Doc;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.validation.constraints.*;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class ValidMapperPipeline extends BasePipeline {

    public ValidMapperPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        logger.debug("Mapping valid mapper:" + type.getQualifiedName());
        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        NotNullStrategy notNullStrategy = (NotNullStrategy) field.getStrategies().get(MapperEnum.NOTNULL.getName());
        NullStrategy nullStrategy = (NullStrategy) field.getStrategies().get(MapperEnum.NULL.getName());
        NotEmptyStrategy notEmptyStrategy = (NotEmptyStrategy) field.getStrategies().get(MapperEnum.NOTEMPTY.getName());
        NotBlankStrategy notBlankStrategy = (NotBlankStrategy) field.getStrategies().get(MapperEnum.NOTBLANK.getName());
        MaxStrategy maxStrategy = (MaxStrategy) field.getStrategies().get(MapperEnum.MAX.getName());
        MinStrategy minStrategy = (MinStrategy) field.getStrategies().get(MapperEnum.MIN.getName());

        if (notNullStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(NotNull.class);
            if (notNullStrategy.getMessage() != null) {
                builder.addMember(" message", "\"" + notNullStrategy.getMessage() + "\"");
            }
            fieldSpec.addAnnotation(builder.build());
        }
        if (nullStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(Null.class);
            if (nullStrategy.getMessage() != null) {
                builder.addMember(" message", "\"" + nullStrategy.getMessage() + "\"");
            }
            fieldSpec.addAnnotation(builder.build());
        }
        if (notEmptyStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(NotEmpty.class);
            if (notEmptyStrategy.getMessage() != null) {
                builder.addMember(" message", "\"" + notEmptyStrategy.getMessage() + "\"");
            }
            fieldSpec.addAnnotation(builder.build());
        }
        if (notBlankStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(NotBlank.class);
            if (notBlankStrategy.getMessage() != null) {
                builder.addMember(" message", "\"" + notBlankStrategy.getMessage() + "\"");
            }
            fieldSpec.addAnnotation(builder.build());
        }
        if (maxStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(Max.class);
            if (maxStrategy.getMessage() != null) {
                builder.addMember("message", "\"" + maxStrategy.getMessage() + "\"");
            }
            builder.addMember("value", String.valueOf(maxStrategy.getMaxValue()));
            fieldSpec.addAnnotation(builder.build());
        }
        if (minStrategy.open()) {
            AnnotationSpec.Builder builder = AnnotationSpec.builder(Min.class);
            if (minStrategy.getMessage() != null) {
                builder.addMember(" message", "\"" + minStrategy.getMessage() + "\"");
            }
            builder.addMember("value", String.valueOf(minStrategy.getMinValue()));
            fieldSpec.addAnnotation(builder.build());
        }

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        return typeSpec;
    }
}
