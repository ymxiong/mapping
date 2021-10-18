package cc.eamon.open.mapping.mapper.support.pipeline.convert.strategy;

import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import com.squareup.javapoet.MethodSpec;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2021-10-18 21:28:45
 */
public interface ConvertStrategy {

    void convertAB(MethodSpec.Builder buildConvertAB, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase);

    void convertBA(MethodSpec.Builder buildConvertBA, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase);

}
