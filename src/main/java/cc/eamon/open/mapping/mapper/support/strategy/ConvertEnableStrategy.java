package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.support.detail.ConvertDetail;

import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 16:53:17
 */
public class ConvertEnableStrategy extends MapperBaseStrategy implements ConvertStrategy {

    private List<ConvertDetail> details;

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public List<TypeMirror> getTypes() {
        List<TypeMirror> types = new ArrayList<>();
        details.forEach(detail-> types.add(detail.getType()));
        return types;
    }

    public List<ConvertDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ConvertDetail> details) {
        this.details = details;
    }

}
