package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperFieldDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperRenameDecorator extends MapperFieldDecorator {

    public MapperRenameDecorator(FieldBuilder fieldBuilder) {
        super(fieldBuilder);
    }

    @Override
    public void decorate() {
        RenameStrategy renameStrategy = (RenameStrategy) fieldBuilder.getMapperField().getStrategies().get(MapperEnum.RENAME.getName());
        fieldBuilder.setName(renameStrategy.getName());
    }
}
