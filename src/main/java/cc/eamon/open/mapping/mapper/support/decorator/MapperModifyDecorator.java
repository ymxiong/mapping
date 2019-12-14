package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperFieldDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import com.squareup.javapoet.TypeName;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperModifyDecorator extends MapperFieldDecorator {
    public MapperModifyDecorator(FieldBuilder fieldBuilder) {
        super(fieldBuilder);
    }

    @Override
    public void decorate() {
        ModifyStrategy modifyStrategy = (ModifyStrategy) fieldBuilder.getMapperField().getStrategies().get(MapperEnum.MODIFY.getName());
        fieldBuilder.setModifyName(modifyStrategy.getModifyName("obj"));
        fieldBuilder.setModifyType(modifyStrategy.getModifyType());
        fieldBuilder.setRecoverName(modifyStrategy.getRecoverName("obj"));
        fieldBuilder.setRecoverType(modifyStrategy.getRecoverType());
        fieldBuilder.setType(TypeName.get(modifyStrategy.getModifyType()));
    }
}
