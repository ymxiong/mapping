package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotBlankDetail;
import cc.eamon.open.mapping.mapper.support.detail.NullDetail;
import cc.eamon.open.mapping.mapper.support.strategy.NotBlankEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NotBlankNormalStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NullEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.NullNormalStrategy;
import cc.eamon.open.mapping.mapper.valid.MapperNotBlank;
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
public class MapperNotBlankFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperNotBlank mapperNotBlank = (MapperNotBlank) annotation;
        // check annotation para length
        if (mapperNotBlank.value().length == 0 || mapperNotBlank.message().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperNotBlank.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperNotBlank.value()[i])) {
                continue;
            }

            // new detail
            NotBlankDetail detail = new NotBlankDetail();

            // set new name
            if (i >= mapperNotBlank.message().length) {
                detail.setMessage(mapperNotBlank.message()[mapperNotBlank.message().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setMessage(mapperNotBlank.message()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new NotBlankNormalStrategy();
        }
        NotBlankEnabledStrategy strategy = new NotBlankEnabledStrategy();
        strategy.setDetail((NotBlankDetail) details.get(0));
        return strategy;
    }

}
