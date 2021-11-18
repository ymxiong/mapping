package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:13:01
 */
public class NotNullNormalStrategy extends MapperBaseStrategy implements NotNullStrategy {

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public Boolean open() {
        return false;
    }
}
