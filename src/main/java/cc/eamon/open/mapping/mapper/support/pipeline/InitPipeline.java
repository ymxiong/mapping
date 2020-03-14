package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.enhancement.Doc;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class InitPipeline extends BasePipeline {

    public InitPipeline() {
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
        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        return typeSpec;
    }
}
