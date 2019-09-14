package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 18:06:29
 */
public interface ExtendsStrategy extends MapperStrategy {

    boolean open();

    String getPackageName();

    String getSuperMapperName();

}
