package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.NotEmptyDetail;
import cc.eamon.open.mapping.mapper.support.detail.NotNullDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class NotEmptyEnabledStrategy extends MapperBaseStrategy implements NotEmptyStrategy {

    private NotEmptyDetail detail;


    @Override
    public String getMessage() {
        return detail.getMessage();
    }

    public void setDetail(NotEmptyDetail detail) {
        this.detail = detail;
    }
}
