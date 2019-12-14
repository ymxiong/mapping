package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperFieldDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.IgnoreStrategy;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperIgnoreDecorator extends MapperFieldDecorator {


    public MapperIgnoreDecorator(FieldBuilder fieldBuilder) {
        super(fieldBuilder);
    }

    @Override
    public void decorate() {
        IgnoreStrategy ignoreStrategy = (IgnoreStrategy) fieldBuilder.getMapperField().getStrategies().get(MapperEnum.IGNORE.getName());
        if (ignoreStrategy.ignore()) {
            fieldBuilder.setIgnore(true);
        }
    }
}
