package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.DocDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 16:53:17
 */
public class DocEnableStrategy extends MapperBaseStrategy implements DocStrategy {
    private DocDetail detail;

    @Override
    public String getNote() {
        return detail.getNote();
    }

    public DocDetail getDetail() {
        return detail;
    }

    public void setDetail(DocDetail detail) {
        this.detail = detail;
    }

}
