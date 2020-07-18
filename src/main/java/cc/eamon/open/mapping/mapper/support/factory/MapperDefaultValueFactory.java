package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperDefaultValue;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.DefaultValueDetail;
import cc.eamon.open.mapping.mapper.support.strategy.DefaultValueEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.DefaultValueNormalStrategy;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2020-04-01 12:49:09
 */
public class MapperDefaultValueFactory extends MapperBaseFactory implements FieldFactory {
    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {

        MapperDefaultValue mapperDefaultValue = (MapperDefaultValue) annotation;
        // check annotation para length
        if (mapperDefaultValue.value().length == 0 || mapperDefaultValue.defaultValue().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperDefaultValue.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperDefaultValue.value()[i])) {
                continue;
            }

            // new detail
            DefaultValueDetail detail = new DefaultValueDetail();

            // set defaultValue
            if (i >= mapperDefaultValue.defaultValue().length) {
                detail.setDefaultValue(mapperDefaultValue.defaultValue()[mapperDefaultValue.defaultValue().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setDefaultValue(mapperDefaultValue.defaultValue()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new DefaultValueNormalStrategy();
        }
        DefaultValueEnableStrategy strategy = new DefaultValueEnableStrategy();
        strategy.setDetail((DefaultValueDetail) details.get(0));
        return strategy;
    }

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }
}
