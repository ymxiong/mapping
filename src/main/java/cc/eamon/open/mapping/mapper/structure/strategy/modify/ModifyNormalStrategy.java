package cc.eamon.open.mapping.mapper.structure.strategy.modify;

import cc.eamon.open.mapping.mapper.StringUtil;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

import javax.lang.model.type.TypeMirror;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:22:09
 */
public class ModifyNormalStrategy extends MapperBaseStrategy implements ModifyStrategy {

    @Override
    public String getModifyName() {
        return "obj.get" + StringUtil.firstWordToUpperCase(getElementName()) + "()";
    }

    @Override
    public String getRecoverName() {
        return "obj.set" + StringUtil.firstWordToUpperCase(getElementName()) + "($)";
    }

    @Override
    public TypeMirror getModifyType() {
        return getElementType();
    }

    @Override
    public TypeMirror getRecoverType() {
        return getElementType();
    }

}
