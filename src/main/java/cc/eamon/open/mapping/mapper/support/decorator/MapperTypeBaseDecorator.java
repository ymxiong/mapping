package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperTypeBaseDecorator extends MapperTypeDecorator {

    public MapperTypeBaseDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public  void  decorate() {

        MapperExtendsDecorator mapperExtendsDecorator=new MapperExtendsDecorator(typeBuilder);
        mapperExtendsDecorator.decorate();
        MapperConvertDecorator mapperConvertDecorator=new MapperConvertDecorator(typeBuilder);
        mapperConvertDecorator.decorate();
        MapperExtraDecorator mapperExtraDecorator=new MapperExtraDecorator(typeBuilder);
        mapperExtraDecorator.decorate();
        MapperTypeDocDecorator mapperTypeDocDecorator=new MapperTypeDocDecorator(typeBuilder);
        mapperTypeDocDecorator.decorate();
    }

}
