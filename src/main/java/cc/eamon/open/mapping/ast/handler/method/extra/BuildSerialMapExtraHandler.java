package cc.eamon.open.mapping.ast.handler.method.extra;

import cc.eamon.open.mapping.ast.handler.method.ExtraMethodHandler;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/9 16:58
 */
public class BuildSerialMapExtraHandler extends ExtraMethodHandler {
    @Override
    public JCTree buildASTMethod() {
        String className = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getSimpleName());
        java.util.List<MapperFieldAST> mapperFields = this.mapperInfo.getMapperFields();
        ListBuffer<JCTree.JCVariableDecl> params = createParams(className, mapperFields);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createMapByBuildMap("buildSerialMap", statements, "String");
        createIfReturn(statements, "obj", "map");
        mapperFields.stream().filter(MapperFieldAST::getExtra).forEach(mapperFieldAST -> {
            MapperField mapperField = mapperFieldAST.getMapperField();
            createMapPutJSON(mapperField, statements);
        });
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString("map"))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnStringMapMethod(true, "buildSerialMapExtra", block, params);
    }

    private void createMapPutJSON(MapperField mapperField, ListBuffer<JCTree.JCStatement> statements){
        String name = mapperField.getSimpleName();
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Literal(name)).append(createToJSONString(treeMaker.Ident(names.fromString(name))));
        statements.append(createMapPutParam(mapperField, expressions));
    }
}
