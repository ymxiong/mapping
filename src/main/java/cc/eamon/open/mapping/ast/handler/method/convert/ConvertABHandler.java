package cc.eamon.open.mapping.ast.handler.method.convert;

import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.List;

/**
 * @author lzr
 * @date 2021/3/13 13:40
 */
public class ConvertABHandler extends ConvertMethodHandler {

    @Override
    public JCTree buildASTMethod() {
        return createConvertMethod(true);
    }
}
