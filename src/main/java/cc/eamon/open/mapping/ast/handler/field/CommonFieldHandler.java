package cc.eamon.open.mapping.ast.handler.field;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzr
 * @date 2021/3/6 13:38
 */
public class CommonFieldHandler extends FieldHandler {
    @Override
    public ListBuffer<JCTree> buildASTField() {
        List<MapperFieldAST> mapperFields = this.mapperInfo.getMapperFields();
        List<MapperFieldAST> notAdded = mapperFields.stream().filter(mapperFieldAST -> !mapperFieldAST.getAdded()).collect(Collectors.toList());
        ListBuffer<JCTree> jcTrees = new ListBuffer<>();
        notAdded.forEach(mapperFieldAST -> {
            MapperField mapperField = mapperFieldAST.getMapperField();
            JCTree.JCVariableDecl jcVariableDecl = this.treeMaker.VarDef(
                    this.treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString(StringUtils.firstWordToLowerCase(mapperField.getSimpleName())),
                    this.treeMaker.Ident(names.fromString(StringUtils.classNameFromQualifiedName(mapperField.getType().toString()))),
                    null
            );
            mapperFieldAST.setAdded(true);
            jcTrees.append(jcVariableDecl);
        });
        return jcTrees;
    }

    @Override
    public void preHandle(AST tree) {

    }
}
