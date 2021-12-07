package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.EnumValueDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:18:30
 */
public class EnumValueEnabledValueStrategy extends MapperBaseStrategy implements EnumValueStrategy {

    private EnumValueDetail detail;


    @Override
    public String getEnumClass() {
        return detail.getEnumClass();
    }

    @Override
    public String getEnumMethod() {
        return detail.getEnumMethod();
    }

    @Override
    public Boolean open() {
        return true;
    }

    public void setDetail(EnumValueDetail detail) {
        this.detail = detail;
    }
}
