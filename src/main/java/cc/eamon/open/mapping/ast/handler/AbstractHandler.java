package cc.eamon.open.mapping.ast.handler;

import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/3 13:21
 */
public interface AbstractHandler {

    BaseHandler buildBaseInfo(MapperInfo mapperInfo, JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names);

    BaseHandler buildBaseInfo(BaseHandler handler);

    void handle(AST tree);

    void executeConvert(HandlerContext ctx, AST tree);

    void preHandle(AST tree);
}
