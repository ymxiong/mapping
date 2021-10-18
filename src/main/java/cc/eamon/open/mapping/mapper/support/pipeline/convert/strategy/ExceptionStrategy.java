package cc.eamon.open.mapping.mapper.support.pipeline.convert.strategy;

import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import com.squareup.javapoet.MethodSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2021-10-18 21:27:47
 */
public class ExceptionStrategy implements ConvertStrategy {

    protected static Logger logger = LoggerFactory.getLogger(ExceptionStrategy.class);

    @Override
    public void convertAB(MethodSpec.Builder buildConvertAB, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase) {
        logger.warn("Mapping build convert type not fit, try to convert:" + modifyStrategy.getModifyType().toString() + " to " + to);
        String fixme = "// FIXME: type[" + from + "] do not fit " + "[" + to + "]";
        buildConvertAB.addStatement(fixme);
        buildConvertAB.addStatement("// to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ")");
    }

    @Override
    public void convertBA(MethodSpec.Builder buildConvertBA, String from, String to, ModifyStrategy modifyStrategy, String fieldUpperCase) {

        logger.warn("Mapping build convert type not fit, try to convert:" + modifyStrategy.getModifyType().toString() + " to " + to);
        String fixme = "// FIXME: type[" + from + "] do not fit " + "[" + to + "]";
        buildConvertBA.addStatement(fixme);
        buildConvertBA.addStatement("// " + modifyStrategy.getRecoverName("to").replace("$", "from.get" + fieldUpperCase + "()"));
    }
}
