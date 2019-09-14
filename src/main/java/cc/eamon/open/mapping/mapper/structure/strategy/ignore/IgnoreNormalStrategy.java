package cc.eamon.open.mapping.mapper.structure.strategy.ignore;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:13:01
 */
public class IgnoreNormalStrategy extends MapperBaseStrategy implements IgnoreStrategy {

    @Override
    public Boolean ignore() {
        return false;
    }

}
