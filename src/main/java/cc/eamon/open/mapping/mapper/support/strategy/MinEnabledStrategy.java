package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.MinDetail;
import cc.eamon.open.mapping.mapper.support.detail.NotBlankDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class MinEnabledStrategy extends MapperBaseStrategy implements MinStrategy {

    private MinDetail detail;


    @Override
    public String getMessage() {
        return detail.getMessage();
    }

    @Override
    public long getMinValue() {
        return detail.getValue();
    }

    public void setDetail(MinDetail detail) {
        this.detail = detail;
    }
}
