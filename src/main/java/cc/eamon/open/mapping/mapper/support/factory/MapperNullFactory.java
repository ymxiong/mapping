package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotNullDetail;
import cc.eamon.open.mapping.mapper.support.detail.NullDetail;
import cc.eamon.open.mapping.mapper.support.strategy.NotNullEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NotNullNormalStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NullEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NullNormalStrategy;
import cc.eamon.open.mapping.mapper.valid.MapperNotNull;
import cc.eamon.open.mapping.mapper.valid.MapperNull;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2021-11-18 10:25:05
 */
public class MapperNullFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperNull mapperNull = (MapperNull) annotation;
        // check annotation para length
        if (mapperNull.value().length == 0 || mapperNull.message().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperNull.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperNull.value()[i])) {
                continue;
            }

            // new detail
            NullDetail detail = new NullDetail();

            // set new name
            if (i >= mapperNull.message().length) {
                detail.setMessage(mapperNull.message()[mapperNull.message().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setMessage(mapperNull.message()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new NullNormalStrategy();
        }
        NullEnabledStrategy strategy = new NullEnabledStrategy();
        strategy.setDetail((NullDetail) details.get(0));
        return strategy;
    }

}
