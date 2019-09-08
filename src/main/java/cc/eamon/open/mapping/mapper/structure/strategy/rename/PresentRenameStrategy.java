package cc.eamon.open.mapping.mapper.structure.strategy.rename;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:28:51
 */
public class PresentRenameStrategy implements RenameStrategy {


    private MapperDetail detail;

    @Override
    public String getName() {
        return null;
    }

    public MapperDetail getDetail() {
        return detail;
    }

    public void setDetail(MapperDetail detail) {
        this.detail = detail;
    }
}
