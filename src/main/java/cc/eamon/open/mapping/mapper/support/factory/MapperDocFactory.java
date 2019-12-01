package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperDoc;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.DocDetail;
import cc.eamon.open.mapping.mapper.support.strategy.DocEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.DocNormalStrategy;

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
public class MapperDocFactory extends MapperBaseFactory implements TypeFactory, FieldFactory {


    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        MapperDoc mapperDoc=(MapperDoc)annotation;
        // check annotation para length
        if (mapperDoc.value().length == 0 || mapperDoc.note().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperDoc.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperDoc.value()[i])) {
                continue;
            }

            // new detail
            DocDetail detail = new DocDetail();

            // get cur note
            if (i >= mapperDoc.note().length) {
                detail.setNote(mapperDoc.note()[mapperDoc.note().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setNote(mapperDoc.note()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new DocNormalStrategy();
        }
        DocEnableStrategy strategy = new DocEnableStrategy();
        strategy.setDetail((DocDetail) details.get(0));

        return strategy;
    }

}
