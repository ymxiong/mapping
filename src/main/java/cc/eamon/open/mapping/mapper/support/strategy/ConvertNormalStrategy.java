package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 16:53:17
 */
public class ConvertNormalStrategy extends MapperBaseStrategy implements ConvertStrategy {

    @Override
    public boolean open() {
        return false;
    }

    @Override
    public List<String> getTypes() {
        return null;
    }
}
