package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotBlankDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class MaxNormalStrategy extends MapperBaseStrategy implements MaxStrategy {


    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public long getMaxValue() {
        return 0;
    }

    @Override
    public Boolean open() {
        return false;
    }

}
