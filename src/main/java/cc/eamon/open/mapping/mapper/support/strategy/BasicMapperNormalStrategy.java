package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-11-20 16:40:47
 */
public class BasicMapperNormalStrategy extends MapperBaseStrategy implements BasicMapperStrategy{

    @Override
    public String getBuildTypeName() {
        return getElementName() + StringUtils.firstWordToUpperCase(getMapper()) + "Mapper";
    }

}
