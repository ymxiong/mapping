package cc.eamon.open.mapping.mapper.support.factory;


import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.EnumValueDetail;
import cc.eamon.open.mapping.mapper.support.strategy.EnumValueEnabledValueStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.EnumValueNormalValueStrategy;
import cc.eamon.open.mapping.mapper.valid.MapperEnumValue;

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
public class MapperEnumValueFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperEnumValue mapperEnum = (MapperEnumValue) annotation;

        // check annotation para length
        if (mapperEnum.value().length == 0) {
            return null;
        }
        if (mapperEnum.enumClass() == null) {
            logger.info("[" + element.getSimpleName() + "]未指明枚举类");
            return null;
        }
        if (mapperEnum.enumMethod() == null) {
            logger.info("[" + element.getSimpleName() + "]未指明枚举方法");
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperEnum.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperEnum.value()[i])) {
                continue;
            }
            // new detail
            EnumValueDetail detail = new EnumValueDetail();
            detail.setEnumClass(mapperEnum.enumClass());
            detail.setEnumMethod(mapperEnum.enumMethod());
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new EnumValueNormalValueStrategy();
        }
        EnumValueEnabledValueStrategy strategy = new EnumValueEnabledValueStrategy();
        strategy.setDetail((EnumValueDetail) details.get(0));
        return strategy;
    }

}
