package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.util.StringUtil;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

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
    public String getModifyType() {
        return getQualifiedTypeName();
    }

    @Override
    public String getRecoverType() {
        return getQualifiedTypeName();
    }

}
