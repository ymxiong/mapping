package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MethodHandler;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/7 14:28
 */
public class BuildEntityHandler extends MethodHandler {


    @Override
    public JCTree buildASTMethod() {
        String className = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getSimpleName());
        String varName = "obj";
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createNewObject(className, varName, statements);
        this.mapperInfo.getMapperFields().stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra())
                .forEach(mapperFieldAST -> {
                    createSetThisStatement(mapperFieldAST.getMapperField(), varName, statements);
                });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString(varName))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createNoArgsMethod(false, className, "buildEntity", block);
    }


}
