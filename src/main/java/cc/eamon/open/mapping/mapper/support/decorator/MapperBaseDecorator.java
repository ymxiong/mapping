package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperStrategy;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;



/**
 * Author:Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperBaseDecorator extends MapperTypeDecorator {

    public MapperBaseDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public void decorate() {
        //init
        MapperType type=typeBuilder.getMapperType();

        // mapper strategies
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) type.getStrategies().get(MapperEnum.MAPPER.getName());

        typeBuilder.setName(basicMapperStrategy.getBuildTypeName());

        typeBuilder.setTypeSpec(TypeSpec.classBuilder(typeBuilder.getName()).addModifiers(Modifier.PUBLIC));

        // define import items
        typeBuilder.setSelf(ClassName.get(type.getPackageName(), type.getSimpleName()));


        //添加一些自己定义的方法
        MapperBaseMethodDecorator mapperBaseMethodDecorator=new MapperBaseMethodDecorator(typeBuilder);
        mapperBaseMethodDecorator.decorate();
        //开始定义处理逻辑
        //处理域注解
        MapperFiledBaseDecorator mapperFiledBaseDecorator=new MapperFiledBaseDecorator(typeBuilder);
        mapperFiledBaseDecorator.decorate();

        //处理方法注解
        MapperMethodBaseDecorator mapperMethodBaseDecorator=new MapperMethodBaseDecorator(typeBuilder);
        mapperMethodBaseDecorator.decorate();

        mapperBaseMethodDecorator.addReturn();
        mapperBaseMethodDecorator.addMethodToType();
        //处理类上注解
        MapperTypeBaseDecorator mapperTypeBaseDecorator=new MapperTypeBaseDecorator(typeBuilder);
        mapperTypeBaseDecorator.decorate();
    }

}
