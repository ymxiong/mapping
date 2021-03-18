package cc.eamon.open.mapping.ast.handler.method.extra;

import cc.eamon.open.mapping.ast.handler.method.ExtraMethodHandler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/9 15:45
 */
public class BuildMapHandler extends ExtraMethodHandler {
    @Override
    public JCTree buildASTMethod() {
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createMap(statements);
        this.mapperInfo.getMapperFields().forEach(mapperFieldAST -> {
                    String simpleName = mapperFieldAST.getMapperField().getSimpleName();
                    JCTree.JCFieldAccess aThis = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString(simpleName));
                    createMapPut(simpleName, aThis, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString("map"))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnMapMethod(false, "buildMap", block, null);
    }
}
