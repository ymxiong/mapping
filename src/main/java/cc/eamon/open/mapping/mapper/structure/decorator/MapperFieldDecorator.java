package cc.eamon.open.mapping.mapper.structure.decorator;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 19:47:53
 */
public interface MapperFieldDecorator extends MapperDecorator{

    void decorate(MapperField mapperField);

}
