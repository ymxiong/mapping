package cc.eamon.open.mapping.mapper.support.pipeline.convert.strategy;

import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import com.squareup.javapoet.MethodSpec;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2021-10-18 21:27:47
 */
public class BigDecimal2IntegerStrategy implements ConvertStrategy {

    @Override
    public void convertAB(MethodSpec.Builder buildConvertAB, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase) {
        buildConvertAB.addStatement("to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ".intValue())");
    }

    @Override
    public void convertBA(MethodSpec.Builder buildConvertBA, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase) {
        buildConvertBA.addStatement(modifyStrategy.getRecoverName("to").replace("$", "BigDecimal.valueOf(from.get" + fieldUpperCase + "())"));
    }
}
