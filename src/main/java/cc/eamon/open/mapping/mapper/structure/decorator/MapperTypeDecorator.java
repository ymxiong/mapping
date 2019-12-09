package cc.eamon.open.mapping.mapper.structure.decorator;

import cc.eamon.open.mapping.mapper.structure.item.MapperType;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 19:47:53
 */
public interface MapperTypeDecorator extends MapperDecorator{

    void decorate(MapperType mapperType);

}
