package cc.eamon.open.mapping.mapper.support.pipeline;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-02-26 17:48:14
 */
public abstract class BasePipeline implements Pipeline {

    protected static Logger logger = LoggerFactory.getLogger(BasePipeline.class);

    private Pipeline pipeline;

    public BasePipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    @Override
    public TypeSpec.Builder buildBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // 先执行前一个工序
        if (pipeline != null) typeSpec = pipeline.buildBefore(type, typeSpec);
        // 再执行自己
        typeSpec = buildTypeBefore(type, typeSpec);
        return typeSpec;
    }

    @Override
    public TypeSpec.Builder buildAfter(MapperType type, TypeSpec.Builder typeSpec) {
        // 先执行前一个工序
        if (pipeline != null) typeSpec = pipeline.buildAfter(type, typeSpec);
        // 再执行自己
        typeSpec = buildTypeAfter(type, typeSpec);
        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildField(MapperField field, FieldSpec.Builder fieldSpec) {
        // 先执行前一个工序
        if (pipeline != null) fieldSpec = pipeline.buildField(field, fieldSpec);
        // 再执行自己
        fieldSpec = buildSelfField(field, fieldSpec);
        return fieldSpec;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public abstract TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec);

    public abstract FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec);

    public abstract TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec);

}
