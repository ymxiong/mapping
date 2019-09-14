package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ExtendsDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 17:49:47
 */
public class ExtendsEnableStrategy extends MapperBaseStrategy implements ExtendsStrategy {

    private ExtendsDetail detail;

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public String getPackageName() {
        return detail.getPackageName();
    }

    @Override
    public String getSuperMapperName() {
        return detail.getSuperMapperName();
    }

    public ExtendsDetail getDetail() {
        return detail;
    }

    public void setDetail(ExtendsDetail detail) {
        this.detail = detail;
    }
}
