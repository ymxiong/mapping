package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.util.StringUtils;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * normal strategy
 * return origin name
 * <p>
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:28:51
 */
public class RenameNormalStrategy extends MapperBaseStrategy implements RenameStrategy {

    /**
     * get origin name
     *
     * @return name
     */
    @Override
    public String getName() {
        return StringUtils.firstWordToLowerCase(getElementName());
    }
}
