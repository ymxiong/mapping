package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 20:23:31
 */
public interface ConvertStrategy extends MapperStrategy {

    boolean open();

    List<TypeMirror> getTypes();

}
