package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.factory.TypeFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ExtendsDetail;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsNormalStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.google.common.collect.Lists;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 16:56:23
 */
public class MapperExtendsFactory extends MapperBaseFactory implements TypeFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }

    public MapperExtendsFactory() {
    }

    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, AnnotationMirror annotationMirror, Element element, String mapper) {
        if (!(element instanceof TypeElement)) {
            return null;
        }

        // find father
        TypeElement typeElement = (TypeElement) element;


        Element superElement = MapperUtils.loadSuperTypeElement(typeElement);
        if (superElement == null) {
            return null;
        }

        Mapper superAnnotation = superElement.getAnnotation(Mapper.class);
        if (superAnnotation == null){
            return null;
        }


        List<String> superMappers = Lists.newArrayList(superAnnotation.value());
        superMappers.add("default");

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (String superMapper : superMappers) {

            // get current value
            if (!mapper.equals(superMapper)) {
                continue;
            }
            ExtendsDetail detail = new ExtendsDetail();

            detail.setPackageName(((PackageElement)superElement.getEnclosingElement()).getQualifiedName().toString());

            detail.setSuperMapperName(superElement.getSimpleName().toString() + StringUtils.firstWordToUpperCase(mapper) + "Mapper");

            // new detail
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0){
            return new ExtendsNormalStrategy();
        }
        ExtendsEnableStrategy strategy = new ExtendsEnableStrategy();
        strategy.setDetail((ExtendsDetail) details.get(0));
        return strategy;
    }


}
