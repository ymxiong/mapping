package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.MaxDetail;
import cc.eamon.open.mapping.mapper.support.detail.NotEmptyDetail;
import cc.eamon.open.mapping.mapper.support.strategy.MaxEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.MaxNormalStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NotEmptyEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NotEmptyNormalStrategy;
import cc.eamon.open.mapping.mapper.valid.MapperMax;
import cc.eamon.open.mapping.mapper.valid.MapperNotEmpty;

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
public class MapperMaxFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperMax mapperMax = (MapperMax) annotation;
        // check annotation para length
        if (mapperMax.value().length == 0 || mapperMax.message().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperMax.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperMax.value()[i])) {
                continue;
            }

            // new detail
            MaxDetail detail = new MaxDetail();

            // set new name
            if (i >= mapperMax.message().length) {
                detail.setMessage(mapperMax.message()[mapperMax.message().length - 1]);
                detail.setValue(Long.valueOf(mapperMax.maxValue()[mapperMax.message().length - 1]));
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setMessage(mapperMax.message()[i]);
                detail.setValue(mapperMax.maxValue()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new MaxNormalStrategy();
        }
        MaxEnabledStrategy strategy = new MaxEnabledStrategy();
        strategy.setDetail((MaxDetail) details.get(0));
        return strategy;
    }

}
