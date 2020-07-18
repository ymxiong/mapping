package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2020-04-01 10:54:25
 */
public class DefaultValueNormalStrategy extends MapperBaseStrategy implements DefaultValueStrategy {

    @Override
    public String getDefaultValue() {
        return null;
    }
}
