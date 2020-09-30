package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * author: lucas
 * date: 2020/7/18
 * email: lucas@eamon.cc
 */
public class ConstructorIgnoreNormalStrategy extends MapperBaseStrategy implements ConstructorIgnoreStrategy {
    @Override
    public Boolean ignore() {
        return false;
    }
}
