package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MethodHandler;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/8 15:22
 */
public class BuildMapperStaticHandler extends MethodHandler {
    @Override
    public JCTree buildASTMethod() {
        String className = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getMapperName());
        String varName = "mapper";
        String paramName = "obj";
        JCTree.JCVariableDecl params = createSingleParam(paramName, treeMaker.Ident(names.fromString(this.mapperInfo.getMapperType().getSimpleName())));
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createNewObject(className, varName, statements);
        this.mapperInfo.getMapperFields().stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra())
                .forEach(mapperFieldAST -> {
                    createAssignStatement(mapperFieldAST.getMapperField(), varName, paramName, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString(varName))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createArgsMethod(true, className, "buildMapper", block, ListBuffer.of(params));
    }

}
