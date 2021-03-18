package cc.eamon.open.mapping.ast.handler;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author lzr
 * @date 2021/3/3 13:29
 */
public class AST {
    private Map<String, ListBuffer<JCTree>> fieldTree = new LinkedHashMap<>();

    private Map<String, ListBuffer<JCTree>> methodTree = new LinkedHashMap<>();

    private ListBuffer<JCTree> classTree = new ListBuffer<>();

    public List<JCTree> build(){
        return this.classTree.toList();
    }


    public Map<String, ListBuffer<JCTree>> getFieldTree() {
        return fieldTree;
    }

    public void setFieldTree(Map<String, ListBuffer<JCTree>> fieldTree) {
        this.fieldTree = fieldTree;
    }

    public Map<String, ListBuffer<JCTree>> getMethodTree() {
        return methodTree;
    }

    public void setMethodTree(Map<String, ListBuffer<JCTree>> methodTree) {
        this.methodTree = methodTree;
    }

    public ListBuffer<JCTree> getClassTree() {
        return classTree;
    }

    public void setClassTree(ListBuffer<JCTree> classTree) {
        this.classTree = classTree;
    }
}
