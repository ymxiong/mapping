package cc.eamon.open.mapping.mapper.structure.factory;

import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;

import javax.lang.model.element.*;
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

    public abstract List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper);

    public abstract MapperStrategy buildStrategy(List<MapperDetail> details);

    @Override
    public MapperStrategy build(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        // check annotation null
        if (annotation == null) {
            return buildStrategy(null, element, mapper);
        }
        List<MapperDetail> details = buildDetails(annotation, annotationMirror, element, mapper);

        // check details length
        if (details == null || 0 == details.size()) {
            return buildStrategy(null, element, mapper);
        }

        // check allow repeat
        if (details.size() > 1 && !allowValueRepeat()) {
            return buildStrategy(null, element, mapper);
        }

        return buildStrategy(details, element, mapper);
    }

    public MapperStrategy buildStrategy(List<MapperDetail> details, Element element, String mapper) {
        MapperStrategy strategy = buildStrategy(details);
        if (strategy != null) {
            strategy.setMapper(mapper);
            strategy.setElementName(element.getSimpleName().toString());
            strategy.setType(element.asType());
        }
        return strategy;
    }

    /**
     * Build strategies
     *
     * @param element javax model element
     * @return list of strategy
     */
    public static Map<String, MapperStrategy> buildStrategies(Element element, String mapper) {

        Map<String, MapperStrategy> strategies = new HashMap<>();

        // build strategy of all value enum
        for (MapperEnum mapperEnum : MapperEnum.values()) {

            // find value processing factory
            MapperFactory factory = mapperEnum.getFactory();

            if (!(element instanceof TypeElement && factory instanceof TypeFactory) &&
                    !(element instanceof VariableElement && factory instanceof FieldFactory) &&
                    !(element instanceof ExecutableElement && factory instanceof MethodFactory)
            ) {
                continue;
            }

            // find value annotation info
            Annotation annotation = element.getAnnotation(mapperEnum.getType());

            // find value annotation mirror info
            AnnotationMirror annotationMirror = MapperUtils.buildAnnotationMirrorByTypeName(element.getAnnotationMirrors(), mapperEnum.getType().getName());

            // build detail
            MapperStrategy strategy = factory.build(annotation, annotationMirror, element, mapper);

            // add detail
            strategies.put(mapperEnum.getName(), strategy);
        }
        return strategies;
    }

}
