package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:28:02
 */
public interface RenameStrategy extends MapperStrategy {

    /**
     * get name of target value field
     * @return name
     */
    String getName();

}
