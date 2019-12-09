package cc.eamon.open.mapping.mapper.structure.decorator;

import cc.eamon.open.mapping.mapper.structure.item.MapperMethod;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 19:47:53
 */
public interface MapperMethodDecorator extends MapperDecorator{

    void decorate(MapperMethod mapperMethod);

}
