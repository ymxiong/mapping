package cc.eamon.open.mapping.mapper.structure.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.support.MapperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:47:53
 */
public abstract class MapperFieldDecorator implements MapperDecorator{
    protected static Logger logger = LoggerFactory.getLogger(MapperBuilder.class);

    protected FieldBuilder fieldBuilder;

    public MapperFieldDecorator(FieldBuilder fieldBuilder) {
        this.fieldBuilder = fieldBuilder;
    }

    public void decorate(){

    };

}
