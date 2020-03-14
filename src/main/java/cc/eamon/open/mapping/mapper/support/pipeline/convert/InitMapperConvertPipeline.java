package cc.eamon.open.mapping.mapper.support.pipeline.convert;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class InitMapperConvertPipeline extends BaseConvertPipeline {

    public InitMapperConvertPipeline() {
        super(null);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {

        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        return typeSpec;
    }
}
