package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.decorator.MapperBaseDecorator;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 20:24:20
 */
public class MapperTypeBuilder {

    private static Logger logger = LoggerFactory.getLogger(MapperBuilder.class);

    public static TypeSpec build(MapperType type) {

        TypeBuilder typeBuilder=new TypeBuilder(type);

        MapperBaseDecorator mapperBaseDecorator=new MapperBaseDecorator(typeBuilder);
        mapperBaseDecorator.decorate();

        return typeBuilder.getTypeSpec().build();
    }

}
