package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.BasicMapperDetail;
import cc.eamon.open.mapping.mapper.util.StringUtils;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-11-20 16:40:47
 */
public class BasicMapperEnableStrategy extends MapperBaseStrategy implements BasicMapperStrategy{

    private BasicMapperDetail detail;

    @Override
    public String getBuildTypeName() {
        return StringUtils.firstWordToUpperCase(detail.getName());
    }

    public BasicMapperDetail getDetail() {
        return detail;
    }

    public void setDetail(BasicMapperDetail detail) {
        this.detail = detail;
    }
}
