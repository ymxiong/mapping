package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 20:21:23
 */
public interface DocStrategy extends MapperStrategy {
    /**
     * get the note of target field
     * @return note
     */
    String getNote();
}
