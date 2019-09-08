package cc.eamon.open.mapping.mapper.structure.factory.support;

import cc.eamon.open.mapping.mapper.MapperRename;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.detail.RenameDetail;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.structure.strategy.rename.OriginRenameStrategy;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 14:25:07
 */
public class MapperRenameFactory extends MapperBaseFactory implements FieldFactory {

    @Override
    public Boolean allowValueRepeat() {
        return false;
    }


    @Override
    public List<MapperDetail> buildDetails(Annotation annotation, Element element, String mapper) {

        MapperRename mapperRename = (MapperRename) annotation;
        // check annotation para length
        if (mapperRename.value().length == 0 || mapperRename.name().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperRename.value().length; i++) {

            // get current mapper
            if (!mapper.equals(mapperRename.value()[i])){
                continue;
            }

            // new detail
            RenameDetail detail = new RenameDetail();

            // set target
            detail.setMapper(mapperRename.value()[i]);

            // set field name
            detail.setElementName(element.getSimpleName().toString());
            detail.setOriginName(element.getSimpleName().toString());

            // set new name
            if (i >= mapperRename.name().length) {
                detail.setFreshName(mapperRename.name()[mapperRename.name().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setFreshName(mapperRename.name()[i]);
            }
            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details, Element element, String mapper) {
        OriginRenameStrategy strategy = new OriginRenameStrategy();
        RenameDetail detail = null;
        if (details == null){
            detail = new RenameDetail();
            detail.setMapper(mapper);
            detail.setElementName(element.getSimpleName().toString());
            detail.setFreshName(element.getSimpleName().toString());
            detail.setOriginName(element.getSimpleName().toString());
        }else {
            detail = (RenameDetail) details.get(0);
        }
        strategy.setDetail(detail);
        return strategy;
    }
}
