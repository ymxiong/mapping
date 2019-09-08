package cc.eamon.open.mapping.mapper.structure.factory;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-08-31 23:18:33
 */
public interface MapperFactory {

    /**
     * Build detail
     * @param annotation java annotation
     * @param element javax model element
     * @return detail
     */
    MapperStrategy build(Annotation annotation, Element element, String mapper);

    /**
     * Is mapper value repeat allowed
     * @return allowValueRepeat
     */
    Boolean allowValueRepeat();

}
