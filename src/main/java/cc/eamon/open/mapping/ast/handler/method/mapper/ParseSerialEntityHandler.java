package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MapMethodHandler;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/9 15:20
 */
public class ParseSerialEntityHandler extends MapMethodHandler {

    @Override
    public JCTree buildASTMethod() {
        String className = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getSimpleName());
        String paramName = "map";
        String varName = "obj";
        JCTree.JCTypeApply map = createMapParam("String", "String");
        JCTree.JCVariableDecl params = createSingleParam(paramName, map);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createNewObject(className, varName, statements);
        createIfReturn(statements, paramName, varName);
        this.mapperInfo.getMapperFields().stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra())
                .forEach(mapperFieldAST -> {
                    createSetJSON(mapperFieldAST.getMapperField(), varName, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString(varName))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnObjectMethod(true, className,"parseSerialEntity", block, ListBuffer.of(params));
    }
}
