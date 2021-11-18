package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotNullDetail;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import cc.eamon.open.mapping.mapper.valid.MapperNotNull;

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
public class MapperNotNullFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperNotNull mapperNotNull = (MapperNotNull) annotation;
        // check annotation para length
        if (mapperNotNull.value().length == 0 || mapperNotNull.message().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperNotNull.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperNotNull.value()[i])) {
                continue;
            }

            // new detail
            NotNullDetail detail = new NotNullDetail();

            // set new name
            if (i >= mapperNotNull.message().length) {
                detail.setMessage(mapperNotNull.message()[mapperNotNull.message().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setMessage(mapperNotNull.message()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new NotNullNormalStrategy();
        }
        NotNullEnabledStrategy strategy = new NotNullEnabledStrategy();
        strategy.setDetail((NotNullDetail) details.get(0));
        return strategy;
    }

}
