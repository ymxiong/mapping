package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.MaxDetail;
import cc.eamon.open.mapping.mapper.support.detail.MinDetail;
import cc.eamon.open.mapping.mapper.support.strategy.MaxEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.MaxNormalStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.MinEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.MinNormalStrategy;
import cc.eamon.open.mapping.mapper.valid.MapperMax;
import cc.eamon.open.mapping.mapper.valid.MapperMin;

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
public class MapperMinFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperMin mapperMin = (MapperMin) annotation;
        // check annotation para length
        if (mapperMin.value().length == 0 || mapperMin.message().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperMin.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperMin.value()[i])) {
                continue;
            }

            // new detail
            MinDetail detail = new MinDetail();

            // set new name
            if (i >= mapperMin.message().length) {
                detail.setMessage(mapperMin.message()[mapperMin.message().length - 1]);
                detail.setValue(Long.valueOf(mapperMin.minValue()[mapperMin.message().length - 1]));
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setMessage(mapperMin.message()[i]);
                detail.setValue(mapperMin.minValue()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new MinNormalStrategy();
        }
        MinEnabledStrategy strategy = new MinEnabledStrategy();
        strategy.setDetail((MinDetail) details.get(0));
        return strategy;
    }

}
