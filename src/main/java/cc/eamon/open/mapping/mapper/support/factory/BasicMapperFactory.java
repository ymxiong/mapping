package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.BasicMapperDetail;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperNormalStrategy;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-11-20 14:30:51
 */
public class BasicMapperFactory extends MapperBaseFactory implements TypeFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        Mapper basicMapper = (Mapper) annotation;

        if (basicMapper.value().length == 0) {
            return null;
        }

        //build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < basicMapper.value().length; i++) {

            // get current value
            if (!mapper.equals(basicMapper.value()[i])) {
                continue;
            }

            // new detail
            BasicMapperDetail detail = new BasicMapperDetail();

            if (i >= basicMapper.name().length) {
                break;
            } else {
                detail.setName(basicMapper.name()[i]);
            }

            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new BasicMapperNormalStrategy();
        }
        BasicMapperEnableStrategy strategy = new BasicMapperEnableStrategy();
        strategy.setDetail((BasicMapperDetail) details.get(0));
        return strategy;
    }


}
