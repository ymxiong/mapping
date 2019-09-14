package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.StringUtil;
import cc.eamon.open.mapping.mapper.support.detail.ModifyDetail;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

import javax.lang.model.type.TypeMirror;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:22:09
 */
public class ModifyEnableStrategy extends MapperBaseStrategy implements ModifyStrategy {

    private ModifyDetail detail;

    @Override
    public String getModifyName() {
        return "obj." + detail.getModifyMethodName() + "(obj.get" + StringUtil.firstWordToUpperCase(getElementName()) + "())";
    }

    @Override
    public String getRecoverName() {
        return "obj.set" + StringUtil.firstWordToUpperCase(getElementName()) + "(obj." + detail.getRecoverMethodName() + "($))";
    }

    @Override
    public TypeMirror getModifyType() {
        return detail.getModifyType();
    }

    @Override
    public TypeMirror getRecoverType() {
        return getElementType();
    }

    public ModifyDetail getDetail() {
        return detail;
    }

    public void setDetail(ModifyDetail detail) {
        this.detail = detail;
    }
}
