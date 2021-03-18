package cc.eamon.open.mapping.ast.handler.field;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.handler.BaseHandler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/5 15:03
 */
public abstract class FieldHandler extends BaseHandler {

    public abstract ListBuffer<JCTree> buildASTField();

    @Override
    public void handle(AST tree) {
        ListBuffer<JCTree> newTree = buildASTField();
        if(newTree != null){
            ListBuffer<JCTree> oldTree = tree.getFieldTree().get(this.mapperInfo.getMapperType().getMapperName());
            oldTree.appendList(newTree);
        }
    }
}
