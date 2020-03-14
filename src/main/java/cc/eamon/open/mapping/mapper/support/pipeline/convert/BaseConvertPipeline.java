package cc.eamon.open.mapping.mapper.support.pipeline.convert;

import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;

import javax.lang.model.type.TypeMirror;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 03:53:35
 */
public abstract class BaseConvertPipeline extends BasePipeline {

    private TypeMirror convertStrategyType;

    private Map<String, TypeMirror> fieldTypeMirrors;

    public BaseConvertPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    public TypeMirror getConvertStrategyType() {
        return convertStrategyType;
    }

    public void setConvertStrategyType(TypeMirror convertStrategyType) {
        // 先执行前一个工序
        if (getPipeline() != null) ((BaseConvertPipeline) getPipeline()).setConvertStrategyType(convertStrategyType);
        // 再执行自己
        this.convertStrategyType = convertStrategyType;
    }

    public Map<String, TypeMirror> getFieldTypeMirrors() {
        return fieldTypeMirrors;
    }

    public void setFieldTypeMirrors(Map<String, TypeMirror> fieldTypeMirrors) {
        // 先执行前一个工序
        if (getPipeline() != null) ((BaseConvertPipeline) getPipeline()).setFieldTypeMirrors(fieldTypeMirrors);
        // 再执行自己
        this.fieldTypeMirrors = fieldTypeMirrors;
    }
}
