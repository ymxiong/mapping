package cc.eamon.open.mapping.ast.handler.method;

import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/9 15:43
 */
public abstract class ExtraMethodHandler extends MapMethodHandler {

    protected ListBuffer<JCTree.JCVariableDecl> createParams(String className, java.util.List<MapperFieldAST> mapperFieldASTS){
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        JCTree.JCVariableDecl obj = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("obj"),
                treeMaker.Ident(names.fromString(className)),
                null
        );
        params.append(obj);
        mapperFieldASTS.stream().filter(MapperFieldAST::getExtra).forEach(mapperFieldAST -> {
            MapperField mapperField = mapperFieldAST.getMapperField();
            JCTree.JCExpression type;
            String mapperType = mapperField.getType().toString();
            if(mapperType.substring(mapperType.lastIndexOf(".")+1).equals("Map")){
                ListBuffer<JCTree.JCExpression> mapExpression = new ListBuffer<>();
                mapExpression.append(treeMaker.Ident(names.fromString(mapperField.getTypeArgs()[0]))).append(treeMaker.Ident(names.fromString(mapperField.getTypeArgs()[1])));
                type = treeMaker.TypeApply(treeMaker.Ident(names.fromString("Map")), mapExpression.toList());
            }else{
                type = treeMaker.Ident(names.fromString(mapperType.substring(mapperType.lastIndexOf(".")+1)));
            }
            if(mapperField.getList()){
                JCTree.JCTypeApply list = treeMaker.TypeApply(treeMaker.Ident(names.fromString("List")), List.of(type));
                JCTree.JCVariableDecl param = treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString(mapperField.getSimpleName()),
                        list,
                        null
                );
                params.append(param);
            }else{
                JCTree.JCVariableDecl param = treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString(mapperField.getSimpleName()),
                        type,
                        null
                );
                params.append(param);
            }
        });
        return params;
    }

    protected void createMapByBuildMap(String methodName, ListBuffer<JCTree.JCStatement> statements, String var2){
        JCTree.JCExpressionStatement mapParam = treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Ident(names.fromString(methodName)),
                List.of(treeMaker.Ident(names.fromString("obj")))
        ));
        ListBuffer<JCTree.JCExpression> expressions = new ListBuffer<>();
        expressions.append(treeMaker.Ident(names.fromString("String"))).append(treeMaker.Ident(names.fromString(var2)));
        JCTree.JCTypeApply mapDef = treeMaker.TypeApply(treeMaker.Ident(names.fromString("Map")), expressions.toList());
        JCTree.JCVariableDecl map = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("map"),
                mapDef,
                mapParam.expr
        );
        statements.append(map);
    }

    protected JCTree.JCStatement createMapPutParam(MapperField mapperField, ListBuffer<JCTree.JCExpression> expression){
        return treeMaker.Exec(treeMaker.Apply(
                List.nil(),
                treeMaker.Select(treeMaker.Ident(names.fromString("map")),
                names.fromString("put")),
                expression.toList()
        ));
    }
}
