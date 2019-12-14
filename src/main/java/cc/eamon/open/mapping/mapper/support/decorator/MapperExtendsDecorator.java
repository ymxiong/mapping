package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import com.squareup.javapoet.ClassName;


/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperExtendsDecorator extends MapperTypeDecorator {


    public MapperExtendsDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }
    @Override
    public void decorate() {
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        if (extendsStrategy.open()) {
            typeBuilder.getTypeSpec().superclass(ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
    }


}
