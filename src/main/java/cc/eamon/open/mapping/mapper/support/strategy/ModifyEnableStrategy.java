package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.util.StringUtils;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ModifyDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:22:09
 */
public class ModifyEnableStrategy extends MapperBaseStrategy implements ModifyStrategy {

    private ModifyDetail detail;

    @Override
    public String getModifyName(String varName) {
        return varName + "." + detail.getModifyMethodName() + "(" + varName + ".get" + StringUtils.firstWordToUpperCase(getElementName()) + "())";
    }

    @Override
    public String getRecoverName(String varName) {
        return varName + ".set" + StringUtils.firstWordToUpperCase(getElementName()) + "(" + varName + "." + detail.getRecoverMethodName() + "($))";
    }

    @Override
    public String getModifyType() {
        return detail.getModifyQualifiedTypeName();
    }

    @Override
    public String getRecoverType() {
        return getQualifiedTypeName();
    }

    public ModifyDetail getDetail() {
        return detail;
    }

    public void setDetail(ModifyDetail detail) {
        this.detail = detail;
    }
}
