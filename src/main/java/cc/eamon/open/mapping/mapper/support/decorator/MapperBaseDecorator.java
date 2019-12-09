package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 17:17:06
 */
public class MapperBaseDecorator implements MapperTypeDecorator {

    @Override
    public void decorate(MapperType type) {
        // mapper strategies
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) type.getStrategies().get(MapperEnum.MAPPER.getName());

        // define new type
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(basicMapperStrategy.getBuildTypeName()).addModifiers(Modifier.PUBLIC);

        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());

        if (extendsStrategy.open()) {
            typeSpec.superclass(ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());



    }

}
