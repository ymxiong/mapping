package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.util.StringUtils;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:22:09
 */
public class ModifyNormalStrategy extends MapperBaseStrategy implements ModifyStrategy {

    @Override
    public String getModifyName(String varName) {
        return varName + ".get" + StringUtils.firstWordToUpperCase(getElementName()) + "()";
    }

    @Override
    public String getRecoverName(String varName) {
        return varName + ".set" + StringUtils.firstWordToUpperCase(getElementName()) + "($)";
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
