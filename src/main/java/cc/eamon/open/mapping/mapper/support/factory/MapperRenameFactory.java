package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperRename;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.RenameDetail;
import cc.eamon.open.mapping.mapper.support.strategy.RenameEnabledStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameNormalStrategy;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 14:25:07
 */
public class MapperRenameFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }


    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperRename mapperRename = (MapperRename) annotation;
        // check annotation para length
        if (mapperRename.value().length == 0 || mapperRename.name().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperRename.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperRename.value()[i])) {
                continue;
            }

            // new detail
            RenameDetail detail = new RenameDetail();

            // set new name
            if (i >= mapperRename.name().length) {
                detail.setFreshName(mapperRename.name()[mapperRename.name().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setFreshName(mapperRename.name()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new RenameNormalStrategy();
        }
        RenameEnabledStrategy strategy = new RenameEnabledStrategy();
        strategy.setDetail((RenameDetail) details.get(0));
        return strategy;
    }
}
