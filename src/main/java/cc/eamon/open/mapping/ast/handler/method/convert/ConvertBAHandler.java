package cc.eamon.open.mapping.ast.handler.method.convert;

import com.sun.tools.javac.tree.JCTree;

/**
 * @author lzr
 * @date 2021/3/13 15:06
 */
public class ConvertBAHandler extends ConvertMethodHandler{
    @Override
    public JCTree buildASTMethod() {
        return createConvertMethod(false);
    }
}
