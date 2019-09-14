package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import javax.lang.model.type.TypeMirror;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 20:20:26
 */
public interface ModifyStrategy extends MapperStrategy {

    String getModifyName();

    String getRecoverName();

    TypeMirror getModifyType();

    TypeMirror getRecoverType();

}
