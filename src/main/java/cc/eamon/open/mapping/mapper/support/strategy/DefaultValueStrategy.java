package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2020-04-01 10:53:30
 */
public interface DefaultValueStrategy extends MapperStrategy {
    /**
     * get the note of target field
     * @return note
     */
    String getDefaultValue();
}
