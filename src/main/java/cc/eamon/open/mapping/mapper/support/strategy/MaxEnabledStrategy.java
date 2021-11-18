package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.MaxDetail;
import cc.eamon.open.mapping.mapper.support.detail.NotBlankDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class MaxEnabledStrategy extends MapperBaseStrategy implements MaxStrategy {

    private MaxDetail detail;


    @Override
    public String getMessage() {
        return detail.getMessage();
    }

    @Override
    public long getMaxValue() {
        return detail.getValue();
    }

    public void setDetail(MaxDetail detail) {
        this.detail = detail;
    }
}
