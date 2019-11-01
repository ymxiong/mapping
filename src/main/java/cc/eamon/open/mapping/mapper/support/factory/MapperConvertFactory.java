package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperConvert;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ConvertDetail;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertNormalStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-11-02 01:47:52
 */
public class MapperConvertFactory extends MapperBaseFactory implements TypeFactory {

    @Override
    public Boolean allowValueRepeat() {
        return true;
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        MapperConvert mapperConvert = (MapperConvert) annotation;

        Map<String, Object> annotationValuesMap = MapperUtils.buildAnnotationFieldsMap(annotationMirror);
        List<String> typeValues = MapperUtils.buildStringAnnotationValueList(annotationValuesMap, "type");

        if (mapperConvert.value().length == 0 || typeValues.size() == 0 || mapperConvert.value().length != typeValues.size()) {
            return null;
        }

        //build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperConvert.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperConvert.value()[i])) {
                continue;
            }

            // new detail
            ConvertDetail detail = new ConvertDetail();

            if (i >= typeValues.size()) {
                // TODO: LOG NOT COMPATIBLE INFO
                break;
            } else {
                detail.setType(typeValues.get(i));
            }

            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0) {
            return new ConvertNormalStrategy();
        }
        ConvertEnableStrategy strategy = new ConvertEnableStrategy();
        List<ConvertDetail> convertDetails = new ArrayList<>();
        details.forEach(detail-> convertDetails.add((ConvertDetail) detail));
        strategy.setDetails(convertDetails);
        return strategy;
    }


}
