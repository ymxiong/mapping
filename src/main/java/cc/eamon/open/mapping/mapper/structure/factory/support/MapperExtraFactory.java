package cc.eamon.open.mapping.mapper.structure.factory.support;

import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
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
    public List<MapperDetail> buildDetails(Annotation annotation, Element element, String mapper) {
        return null;
    }
}
