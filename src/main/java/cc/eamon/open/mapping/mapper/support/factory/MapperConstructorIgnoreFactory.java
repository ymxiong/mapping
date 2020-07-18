package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperConstructorIgnore;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ConstructorIgnoreDetail;
import cc.eamon.open.mapping.mapper.support.strategy.ConstructorIgnoreEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ConstructorIgnoreNormalStrategy;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: lucas
 * @date: 2020/7/18
 * @email: lucas@eamon.cc
 */
public class MapperConstructorIgnoreFactory extends MapperBaseFactory implements FieldFactory {
    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        MapperConstructorIgnore mapperConstructorIgnore = (MapperConstructorIgnore) annotation;
        // check annotation para length
        if (mapperConstructorIgnore.value().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperConstructorIgnore.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperConstructorIgnore.value()[i])){
                continue;
            }
            // new detail
            details.add(new ConstructorIgnoreDetail());
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0){
            return new ConstructorIgnoreNormalStrategy();
        }
        return new ConstructorIgnoreEnableStrategy();
    }

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }
}
