package cc.eamon.open.mapping.ast.handler.method;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.handler.BaseHandler;
import cc.eamon.open.mapping.ast.item.ModifyStrategyAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/7 14:13
 */
public abstract class MethodHandler extends BaseHandler {
    public abstract JCTree buildASTMethod();

    @Override
    public void preHandle(AST tree) {

    }

    protected void createNewObject(String className, String varName, ListBuffer<JCTree.JCStatement> statements){
        JCTree.JCNewClass jcNewClass = treeMaker.NewClass(null, List.nil(), treeMaker.Ident(names.fromString(className)), List.nil(), null);
        JCTree.JCVariableDecl jcVariableDecl = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString(varName),
                treeMaker.Ident(names.fromString(className)),
                jcNewClass
        );
        statements.append(jcVariableDecl);
    }

    public void createIfReturn(ListBuffer<JCTree.JCStatement> statements, String var1, String var2){
        JCTree.JCMethodInvocation apply = treeMaker.Apply(
                List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("Objects")),
                        names.fromString("isNull")
                ),
                List.of(treeMaker.Ident(names.fromString(var1)))
        );
        JCTree.JCReturn map1 = treeMaker.Return(treeMaker.Ident(names.fromString(var2)));
        JCTree.JCIf anIf = treeMaker.If(apply, map1, null);
        statements.append(anIf);
    }

    protected void createSetStatement(MapperField mapperField, JCTree.JCExpression expression, String varName, ListBuffer<JCTree.JCStatement> statements){
        String fieldName = mapperField.getSimpleName();
        ModifyStrategy mapperStrategy = (ModifyStrategy) mapperField.getStrategies().get(MapperEnum.MODIFY.getName());
        JCTree.JCExpressionStatement statement = this.treeMaker.Exec(
                this.treeMaker.Apply(
                        List.nil(),
                        this.treeMaker.Select(
                                this.treeMaker.Ident(names.fromString(varName)),
                                names.fromString("set" + StringUtils.firstWordToUpperCase(fieldName))
                        ),
                        List.of(ModifyStrategyAST.buildRecover(mapperStrategy, expression, varName, this.treeMaker, this.names))
                )
        );
        statements.append(statement);
    }

    protected void createSetThisStatement(MapperField mapperField, String varName, ListBuffer<JCTree.JCStatement> statements){
        String fieldName = mapperField.getSimpleName();
        JCTree.JCFieldAccess aThis = treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString(fieldName));
        createSetStatement(mapperField, aThis, varName, statements);
    }

    protected void createAssignStatement(MapperField mapperField, String leftName, String rightName, ListBuffer<JCTree.JCStatement> statements){
        String fieldName = mapperField.getSimpleName();
        ModifyStrategy strategy = (ModifyStrategy) mapperField.getStrategies().get(MapperEnum.MODIFY.getName());
        JCTree.JCExpressionStatement statement = treeMaker.Exec(
                treeMaker.Assign(
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString(leftName)),
                                names.fromString(fieldName)
                        ),
                        ModifyStrategyAST.buildModify(strategy, fieldName, rightName, treeMaker, names)
                )
        );
        statements.append(statement);
    }

    protected JCTree createNoArgsMethod(Boolean isStatic, String className, String methodName, JCTree.JCBlock block){
        return this.treeMaker.MethodDef(
                treeMaker.Modifiers(isStatic?Flags.PUBLIC+Flags.STATIC:Flags.PUBLIC),
                names.fromString(methodName),
                treeMaker.Ident(names.fromString(className)),
                List.nil(),
                List.nil(),
                List.nil(),
                block,
                null
        );
    }

    protected JCTree createArgsMethod(Boolean isStatic, String className, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params){
        return this.treeMaker.MethodDef(
                treeMaker.Modifiers(isStatic?Flags.PUBLIC+Flags.STATIC:Flags.PUBLIC),
                names.fromString(methodName),
                treeMaker.Ident(names.fromString(className)),
                List.nil(),
                params.toList(),
                List.nil(),
                block,
                null
        );
    }

    protected JCTree.JCVariableDecl createSingleParam(String paramName, JCTree.JCExpression type){
        return treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString(paramName),
                type,
                null
        );
    }

    public JCTree createReturnObjectMethod(Boolean isStatic, String className, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params) {
        JCTree.JCIdent obj = treeMaker.Ident(names.fromString(className));
        return createReturnMethod(isStatic, obj, methodName, block, params);

    }

    protected JCTree createReturnMethod(Boolean isStatic, JCTree.JCExpression expression, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params) {
        return treeMaker.MethodDef(
                treeMaker.Modifiers(isStatic?Flags.PUBLIC+Flags.STATIC:Flags.PUBLIC),
                names.fromString(methodName),
                expression,
                List.nil(),
                params!=null?params.toList():List.nil(),
                List.nil(),
                block,
                null
        );
    }

    @Override
    public void handle(AST tree) {
        JCTree newTree = buildASTMethod();
        if(newTree != null){
            ListBuffer<JCTree> oldTree = tree.getMethodTree().get(this.mapperInfo.getMapperType().getMapperName());
            oldTree.append(newTree);
        }
    }
}