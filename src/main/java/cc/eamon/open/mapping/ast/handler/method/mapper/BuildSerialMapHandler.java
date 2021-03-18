package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MapMethodHandler;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/8 22:12
 */
public class BuildSerialMapHandler extends MapMethodHandler {

    @Override
    public JCTree buildASTMethod() {
        String paramName = "obj";
        JCTree.JCVariableDecl params = createSingleParam(paramName, treeMaker.Ident(names.fromString(this.mapperInfo.getMapperType().getSimpleName())));
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createStringMap(statements);
        createIfReturn(statements, paramName, "map");
        this.mapperInfo.getMapperFields().stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra())
                .forEach(mapperFieldAST -> {
                    createMapPutJSON(mapperFieldAST.getMapperField(), paramName, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString("map"))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnStringMapMethod(true, "buildSerialMap", block, ListBuffer.of(params));
    }
}
