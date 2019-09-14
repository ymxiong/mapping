package cc.eamon.open.mapping.mapper.structure.strategy.rename;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;
import cc.eamon.open.mapping.mapper.structure.detail.RenameDetail;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:28:51
 */
public class RenameEnabledStrategy extends MapperBaseStrategy implements RenameStrategy {

    private RenameDetail detail;

    @Override
    public String getName() {
        return detail.getFreshName();
    }

    public MapperDetail getDetail() {
        return detail;
    }

    public void setDetail(RenameDetail detail) {
        this.detail = detail;
    }
}
