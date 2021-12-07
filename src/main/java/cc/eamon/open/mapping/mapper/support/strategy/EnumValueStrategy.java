package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:12:13
 */
public interface EnumValueStrategy extends MapperStrategy {

    String getEnumClass();

    String getEnumMethod();

    String getMessage();

    Boolean open();
}
