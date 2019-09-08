package cc.eamon.open.mapping.mapper.structure.factory.support;

import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 14:25:07
 */
public class MapperModifyFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, Element element, String mapper) {
        return null;
    }

}
