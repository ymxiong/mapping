package cc.eamon.open.mapping.mapper.structure.strategy.rename;

import cc.eamon.open.mapping.mapper.structure.detail.RenameDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:28:51
 */
public class OriginRenameStrategy implements RenameStrategy {

    private RenameDetail detail;

    @Override
    public String getName() {
        return detail.getFreshName();
    }

    public RenameDetail getDetail() {
        return detail;
    }

    public void setDetail(RenameDetail detail) {
        this.detail = detail;
    }
}
