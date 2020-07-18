package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.DefaultValueDetail;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2020-04-01 10:54:25
 */
public class DefaultValueEnableStrategy extends MapperBaseStrategy implements DefaultValueStrategy {

    private DefaultValueDetail detail;

    @Override
    public String getDefaultValue() {
        return detail.getDefaultValue();
    }

    public MapperDetail getDetail() {
        return detail;
    }

    public void setDetail(DefaultValueDetail detail) {
        this.detail = detail;
    }
}
