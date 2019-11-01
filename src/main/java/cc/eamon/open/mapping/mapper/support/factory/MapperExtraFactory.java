package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperExtra;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ExtraDetail;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraNormalStrategy;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 14:19:38
 */
public class MapperExtraFactory extends MapperBaseFactory implements TypeFactory {

    @Override
    public Boolean allowValueRepeat() {
        return true;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        MapperExtra mapperExtra = (MapperExtra) annotation;
        // check annotation para length
        if (mapperExtra.value().length == 0 || mapperExtra.name().length == 0 || mapperExtra.type().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperExtra.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperExtra.value()[i])) {
                continue;
            }

            // new detail
            ExtraDetail detail = new ExtraDetail();
            detail.setMapper(mapper);

            if (i >= mapperExtra.name().length) {
                // TODO: LOG NOT COMPATIBLE INFO
                break;
            } else {
                detail.setName(mapperExtra.name()[i]);
            }


            if (i >= mapperExtra.type().length) {
                // TODO: LOG NOT COMPATIBLE INFO
                break;
            } else {
                detail.setType(mapperExtra.type()[i]);
            }

            if (mapperExtra.list().length == 0) {
                detail.setList(false);
            } else if (i >= mapperExtra.list().length) {
                detail.setList(mapperExtra.list()[mapperExtra.list().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setList(mapperExtra.list()[i]);
            }

            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new ExtraNormalStrategy();
        }
        ExtraEnableStrategy strategy = new ExtraEnableStrategy();
        List<ExtraDetail> extraDetails = new ArrayList<>();
        details.forEach(detail -> extraDetails.add((ExtraDetail) detail));
        strategy.setDetails(extraDetails);
        return strategy;
    }

}
