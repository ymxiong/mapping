package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotNullDetail;
import cc.eamon.open.mapping.mapper.support.detail.NullDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class NullEnabledStrategy extends MapperBaseStrategy implements NullStrategy {

    private NullDetail detail;


    @Override
    public String getMessage() {
        return detail.getMessage();
    }

    public void setDetail(NullDetail detail) {
        this.detail = detail;
    }
}
