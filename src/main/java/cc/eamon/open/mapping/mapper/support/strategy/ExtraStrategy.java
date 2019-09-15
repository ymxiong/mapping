package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 20:20:57
 */
public interface ExtraStrategy extends MapperStrategy {

    boolean open();

    List<MapperField> getMapperFields();

}
