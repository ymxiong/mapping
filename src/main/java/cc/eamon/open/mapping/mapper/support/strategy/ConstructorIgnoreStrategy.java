package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * author: lucas
 * date: 2020/7/18
 * email: lucas@eamon.cc
 */
public interface ConstructorIgnoreStrategy extends MapperStrategy {
    Boolean ignore();
}
