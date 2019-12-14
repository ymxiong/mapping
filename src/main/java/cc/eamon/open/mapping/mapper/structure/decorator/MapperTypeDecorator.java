package cc.eamon.open.mapping.mapper.structure.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.support.MapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-03 19:47:53
 */
public abstract class MapperTypeDecorator implements MapperDecorator{
     protected static Logger logger = LoggerFactory.getLogger(MapperBuilder.class);

     protected TypeBuilder typeBuilder;

     public MapperTypeDecorator(TypeBuilder typeBuilder) {
          this.typeBuilder = typeBuilder;
     }

     public void decorate(){

     };


}
