package cc.eamon.open.mapping.ast.handler.method;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/8 19:44
 */
public interface MapMethod extends JSONMethod{

    void createMap(ListBuffer<JCTree.JCStatement> statements);

    void createMapPut(MapperField mapperField, String paramName, ListBuffer<JCTree.JCStatement> statements);

    JCTree createReturnMapMethod(Boolean isStatic, String methodName, JCTree.JCBlock block, ListBuffer<JCTree.JCVariableDecl> params);

    JCTree.JCTypeApply createMapParam(String var1, String var2);

    void createSetMapStatement(MapperField mapperField, String varName, ListBuffer<JCTree.JCStatement> statements);
}
