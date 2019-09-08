package cc.eamon.open.mapping.mapper.structure.factory;

import cc.eamon.open.mapping.mapper.structure.factory.support.MapperEnum;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 17:33:57
 */
public abstract class MapperBaseFactory implements MapperFactory {

    @Override
    public List<MapperDetail> build(Annotation annotation, Element element, String mapper) {
        // check annotation null
        if (annotation == null) {
            return null;
        }
        List<MapperDetail> details = buildDetails(annotation, element, mapper);

        // check details length
        if (details == null || 0 == details.size()) {
            return null;
        }

        // check allow repeat
        if (details.size() > 1 && !allowValueRepeat()) {
            return null;
        }

        return details;
    }

    public abstract List<MapperDetail> buildDetails(Annotation annotation, Element element, String mapper);


    /**
     * Build field details
     *
     * @param element javax model element
     * @return list of strategy
     */
    public static Map<String, List<MapperDetail>> buildFieldDetails(Element element, String mapper) {

        Map<String, List<MapperDetail>> details = new HashMap<>();

        // build strategy of all mapper enum
        for (MapperEnum mapperEnum : MapperEnum.values()) {

            // find mapper processing factory
            MapperFactory factory = mapperEnum.getFactory();

            // find mapper annotation info
            Annotation annotation = element.getAnnotation(mapperEnum.getType());

            // build detail
            List<MapperDetail> detailList = factory.build(annotation, element, mapper);

            // add detail
            details.put(mapperEnum.getName(), detailList);
        }

        return details;
    }

}
