package cc.eamon.open.mapping.mapper.support.pipeline.mapper;

import cc.eamon.open.mapping.enhancement.Doc;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import javax.validation.constraints.*;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class InitMapperPipeline extends BasePipeline {

    public InitMapperPipeline() {
        super(null);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // mapper strategies
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) type.getStrategies().get(MapperEnum.MAPPER.getName());

        // define new type
        typeSpec = TypeSpec.classBuilder(basicMapperStrategy.getBuildTypeName()).addModifiers(Modifier.PUBLIC);

        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());
        DocStrategy typeDocStrategy = (DocStrategy) type.getStrategies().get(MapperEnum.DOC.getName());


        if (extendsStrategy.open()) {
            typeSpec.superclass(ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        if (typeDocStrategy.getNote() != null) {
            AnnotationSpec annotationSpec = AnnotationSpec.builder(Doc.class)
                    .addMember("note", "\"" + typeDocStrategy.getNote() + "\"")
                    .build();
            typeSpec.addAnnotation(annotationSpec);
        }

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());
        DocStrategy fieldDocStrategy = (DocStrategy) field.getStrategies().get(MapperEnum.DOC.getName());
        DefaultValueStrategy defaultValueStrategy = (DefaultValueStrategy) field.getStrategies().get(MapperEnum.DEFAULTVALUE.getName());
        // TODO:添加各种注解
        NotNullStrategy notNullStrategy = (NotNullStrategy) field.getStrategies().get(MapperEnum.NOTNULL.getName());
        NullStrategy nullStrategy = (NullStrategy) field.getStrategies().get(MapperEnum.NULL.getName());
        NotEmptyStrategy notEmptyStrategy = (NotEmptyStrategy) field.getStrategies().get(MapperEnum.NOTEMPTY.getName());
        NotBlankStrategy notBlankStrategy = (NotBlankStrategy) field.getStrategies().get(MapperEnum.NOTBLANK.getName());
        MaxStrategy maxStrategy = (MaxStrategy) field.getStrategies().get(MapperEnum.MAX.getName());
        MinStrategy minStrategy = (MinStrategy) field.getStrategies().get(MapperEnum.MIN.getName());


        fieldSpec = FieldSpec.builder(
                TypeName.get(modifyStrategy.getModifyType()),
                renameStrategy.getName(),
                Modifier.PUBLIC);

        if (fieldDocStrategy.getNote() != null) {
            AnnotationSpec annotationSpec = AnnotationSpec.builder(Doc.class)
                    .addMember(" note", "\"" + fieldDocStrategy.getNote() + "\"")
                    .build();
            fieldSpec.addAnnotation(annotationSpec);
        }
        if (defaultValueStrategy.getDefaultValue() != null) {
            fieldSpec.initializer(defaultValueStrategy.getDefaultValue());
        }
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
