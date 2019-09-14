package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 17:52:46
 */
public class ExtendsNormalStrategy extends MapperBaseStrategy implements ExtendsStrategy {

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public String getPackageName() {
        return null;
    }

    @Override
    public String getSuperMapperName() {
        return null;
    }

}
