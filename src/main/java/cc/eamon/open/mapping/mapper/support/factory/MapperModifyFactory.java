package cc.eamon.open.mapping.mapper.support.factory;

import cc.eamon.open.mapping.mapper.MapperModify;
import cc.eamon.open.mapping.mapper.structure.context.MapperContextHolder;
import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.support.detail.ModifyDetail;
import cc.eamon.open.mapping.mapper.structure.factory.FieldFactory;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyNormalStrategy;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
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
        MapperModify mapperModify = (MapperModify) annotation;
        // check annotation para length
        if (mapperModify.value().length == 0 || mapperModify.modify().length == 0 || mapperModify.recover().length == 0) {
            return null;
        }

        // build detail
        List<MapperDetail> details = new ArrayList<>();
        for (int i = 0; i < mapperModify.value().length; i++) {

            // get current value
            if (!mapper.equals(mapperModify.value()[i])) {
                continue;
            }

            // new detail
            ModifyDetail detail = new ModifyDetail();

            if (i >= mapperModify.modify().length) {
                detail.setModifyMethodName(mapperModify.modify()[mapperModify.modify().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setModifyMethodName(mapperModify.modify()[i]);
            }

            // TODO: check method exist
            Type.MethodType methodType = (Type.MethodType) MapperContextHolder.get().getMethodMap().get(detail.getModifyMethodName()).asType();
            detail.setModifyType(methodType.getReturnType());

            if (i >= mapperModify.recover().length) {
                detail.setRecoverMethodName(mapperModify.recover()[mapperModify.recover().length - 1]);
                // TODO: LOG NOT COMPATIBLE INFO
            } else {
                detail.setRecoverMethodName(mapperModify.recover()[i]);
            }

            details.add(detail);
        }

        return details;
    }

    @Override
    public MapperStrategy buildStrategy(List<MapperDetail> details) {
        if (details == null || details.size() == 0){
            return new ModifyNormalStrategy();
        }
        ModifyEnableStrategy strategy = new ModifyEnableStrategy();
        strategy.setDetail((ModifyDetail) details.get(0));
        return strategy;
    }

}
