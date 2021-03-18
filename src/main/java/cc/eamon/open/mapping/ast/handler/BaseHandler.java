package cc.eamon.open.mapping.ast.handler;

import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/5 9:49
 */
public abstract class BaseHandler implements AbstractHandler {

    protected MapperInfo mapperInfo;

    protected JCTree.JCClassDecl jcClassDecl;

    protected TreeMaker treeMaker;

    protected Names names;

    @Override
    public BaseHandler buildBaseInfo(MapperInfo mapperInfo, JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        this.mapperInfo = mapperInfo;
        this.jcClassDecl = jcClassDecl;
        this.treeMaker = treeMaker;
        this.names = names;
        return this;
    }

    @Override
    public BaseHandler buildBaseInfo(BaseHandler handler){
        this.mapperInfo = handler.mapperInfo;
        this.jcClassDecl = handler.jcClassDecl;
        this.treeMaker = handler.treeMaker;
        this.names = handler.names;
        return this;
    }

    @Override
    public void executeConvert(HandlerContext ctx, AST tree) {
        preHandle(tree);
        handle(tree);
        ctx.fireConvertExecuted(tree);
    }

}
