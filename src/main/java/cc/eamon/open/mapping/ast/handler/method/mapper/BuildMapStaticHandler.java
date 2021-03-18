package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MapMethodHandler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/8 16:01
 */
public class BuildMapStaticHandler extends MapMethodHandler {
    @Override
    public JCTree buildASTMethod() {
        String paramName = "obj";
        JCTree.JCVariableDecl params = createSingleParam(paramName, treeMaker.Ident(names.fromString(this.mapperInfo.getMapperType().getSimpleName())));
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createMap(statements);
        createIfReturn(statements, paramName, "map");
        this.mapperInfo.getMapperFields().stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra())
                .forEach(mapperFieldAST -> {
                    createMapPut(mapperFieldAST.getMapperField(), paramName, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString("map"))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnMapMethod(true, "buildMap", block, ListBuffer.of(params));
    }

}
