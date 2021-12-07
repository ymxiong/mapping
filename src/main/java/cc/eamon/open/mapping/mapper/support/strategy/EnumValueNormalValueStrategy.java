package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class EnumValueNormalValueStrategy extends MapperBaseStrategy implements EnumValueStrategy {

    @Override
    public String getEnumClass() {
        return null;
    }

    @Override
    public String getEnumMethod() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public Boolean open() {
        return false;
    }
}
