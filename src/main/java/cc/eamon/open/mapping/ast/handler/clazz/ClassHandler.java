package cc.eamon.open.mapping.ast.handler.clazz;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.handler.BaseHandler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/5 9:36
 */
public abstract class ClassHandler extends BaseHandler {

    public abstract JCTree buildASTClass(AST tree);

    public abstract ListBuffer<JCTree> getClassContent(AST tree, String mapperName);

    @Override
    public void handle(AST tree) {
        JCTree newTree = buildASTClass(tree);
        if(newTree != null){
            tree.getClassTree().append(newTree);
        }
    }
}
