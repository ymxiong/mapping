package cc.eamon.open.mapping.ast.handler.method;

import cc.eamon.open.mapping.ast.item.ModifyStrategyAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/8 22:18
 */
public abstract class MapMethodHandler extends MethodHandler implements MapMethod {

    @Override
    public JCTree.JCTypeApply createMapParam(String var1, String var2) {
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Ident(names.fromString(var1))).append(treeMaker.Ident(names.fromString(var2)));
        return treeMaker.TypeApply(treeMaker.Ident(names.fromString("Map")), expressions.toList());
    }

    @Override
    public void createMap(ListBuffer<JCTree.JCStatement> statements){

        JCTree.JCTypeApply map = createMapParam("String", "Object");
        JCTree.JCTypeApply linkedHashMap = treeMaker.TypeApply(treeMaker.Ident(names.fromString("LinkedHashMap")), List.nil());
        JCTree.JCNewClass jcNewClass = treeMaker.NewClass(null, List.nil(), linkedHashMap, List.nil(), null);
        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("map"),
                map,
                jcNewClass
        );
        statements.append(jcVariableDecl);
    }

    public void createStringMap(ListBuffer<JCTree.JCStatement> statements){

        JCTree.JCTypeApply map = createMapParam("String", "String");
        JCTree.JCTypeApply linkedHashMap = treeMaker.TypeApply(treeMaker.Ident(names.fromString("LinkedHashMap")), List.nil());
        JCTree.JCNewClass jcNewClass = treeMaker.NewClass(null, List.nil(), linkedHashMap, List.nil(), null);
        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("map"),
                map,
                jcNewClass
        );
        statements.append(jcVariableDecl);
    }

    @Override
    public void createMapPut(MapperField mapperField, String paramName, ListBuffer<JCTree.JCStatement> statements){
        String fieldName = mapperField.getSimpleName();
        ModifyStrategy mapperStrategy = (ModifyStrategy) mapperField.getStrategies().get(MapperEnum.MODIFY.getName());
        createMapPut(fieldName, ModifyStrategyAST.buildModify(mapperStrategy, fieldName, paramName, treeMaker, names), statements);
    }

    public void createMapPut(String fieldName, JCTree.JCExpression expression, ListBuffer<JCTree.JCStatement> statements){
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Literal(fieldName)).append(expression);
        JCTree.JCExpressionStatement exec = treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("map")),
                        names.fromString("put")
                ),
                expressions.toList()
        ));
        statements.append(exec);
    }

    @Override
    public void createMapPutJSON(MapperField mapperField, String paramName, ListBuffer<JCTree.JCStatement> statements) {
        String fieldName = mapperField.getSimpleName();
        ModifyStrategy mapperStrategy = (ModifyStrategy) mapperField.getStrategies().get(MapperEnum.MODIFY.getName());
        JCTree.JCExpression toJSONString = createToJSONString(ModifyStrategyAST.buildModify(mapperStrategy, fieldName, paramName, treeMaker, names));
        createMapPut(fieldName, toJSONString, statements);
    }

    @Override
    public JCTree createReturnMapMethod(Boolean isStatic, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params){
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Ident(names.fromString("String"))).append(treeMaker.Ident(names.fromString("Object")));
        JCTree.JCTypeApply map = treeMaker.TypeApply(treeMaker.Ident(names.fromString("Map")), expressions.toList());
        return createReturnMethod(isStatic, map, methodName, block, params);
    }

    public JCTree createReturnStringMapMethod(Boolean isStatic, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params){
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Ident(names.fromString("String"))).append(treeMaker.Ident(names.fromString("String")));
        JCTree.JCTypeApply map = treeMaker.TypeApply(treeMaker.Ident(names.fromString("Map")), expressions.toList());
        return createReturnMethod(isStatic, map, methodName, block, params);
    }

    protected JCTree.JCExpression createToJSONString(JCTree.JCExpression expression){
        return treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("JSONObject")),
                        names.fromString("toJSONString")
                ),
                List.of(expression)
        )).expr;
    }

    @Override
    public void createSetMapStatement(MapperField mapperField, String varName, ListBuffer<JCTree.JCStatement> statements) {
        String fieldName = mapperField.getSimpleName();
        String type = mapperField.getType().toString();
        JCTree.JCExpression expr = treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("map")),
                        names.fromString("get")
                ),
                List.of(treeMaker.Literal(fieldName))
        )).expr;
        JCTree.JCTypeCast mapGet = treeMaker.TypeCast(treeMaker.Ident(names.fromString(type.substring(type.lastIndexOf(".")+1))), expr);
        createSetStatement(mapperField, mapGet, varName, statements);
    }

    @Override
    public void createSetJSON(MapperField mapperField, String varName, ListBuffer<JCTree.JCStatement> statements) {
        JCTree.JCExpression jsonParseObject = createJSONParseObject(mapperField);
        createSetStatement(mapperField, jsonParseObject, varName, statements);
    }

    @Override
    public JCTree.JCExpression createJSONParseObject(MapperField mapperField) {
        String fieldName = mapperField.getSimpleName();
        String type = mapperField.getType().toString();
        ListBuffer<JCTree.JCExpression> params = new ListBuffer<>();
        JCTree.JCExpressionStatement mapGet = treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("map")),
                        names.fromString("get")
                ),
                List.of(treeMaker.Literal(fieldName))
        ));
        params.append(mapGet.expr).append(treeMaker.Select(treeMaker.Ident(names.fromString(type.substring(type.lastIndexOf(".")+1))), names.fromString("class")));
        return treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("JSONObject")),
                        names.fromString("parseObject")
                ),
                params.toList()
                )
        ).expr;
    }
}
