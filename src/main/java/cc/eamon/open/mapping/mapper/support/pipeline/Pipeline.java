package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-02-26 17:43:34
 */
public interface Pipeline {

    TypeSpec.Builder buildBefore(MapperType type, TypeSpec.Builder typeSpec);

    FieldSpec.Builder buildField(MapperField field, FieldSpec.Builder fieldSpec);

    TypeSpec.Builder buildAfter(MapperType type, TypeSpec.Builder typeSpec);

}
