package cc.eamon.open.mapping.ast.handler.method;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/9 15:24
 */
public interface JSONMethod {

    void createMapPutJSON(MapperField mapperField, String paramName, ListBuffer<JCTree.JCStatement> statements);

    void createSetJSON(MapperField mapperField, String varName, ListBuffer<JCTree.JCStatement> statements);

    JCTree.JCExpression createJSONParseObject(MapperField mapperField);
}
