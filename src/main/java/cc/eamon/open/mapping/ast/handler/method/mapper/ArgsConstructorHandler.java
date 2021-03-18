package cc.eamon.open.mapping.ast.handler.method.mapper;

import cc.eamon.open.mapping.ast.handler.method.MethodHandler;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

import java.util.stream.Collectors;

/**
 * @author lzr
 * @date 2021/3/9 18:30
 */
public class ArgsConstructorHandler extends MethodHandler {
    @Override
    public JCTree buildASTMethod() {
        java.util.List<MapperFieldAST> mapperFields = this.mapperInfo.getMapperFields();
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        java.util.List<MapperFieldAST> filterMapper = mapperFields.stream().filter(mapperFieldAST -> !mapperFieldAST.getExtra()).collect(Collectors.toList());
        ListBuffer<JCTree.JCVariableDecl> params = createParams(filterMapper);
        filterMapper.forEach(mapperFieldAST -> {
            createSimpleAssign(mapperFieldAST.getMapperField(), statements);
        });
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC),
                names.fromString(StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getMapperName())),
                null,
                List.nil(),
                List.nil(),
                List.nil(),
                block,
                null);
    }

    private void createSimpleAssign(MapperField mapperField, ListBuffer<JCTree.JCStatement> statements){
        String fieldName = mapperField.getSimpleName();
        JCTree.JCExpressionStatement aThis = treeMaker.Exec(treeMaker.Assign(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("this")),
                        names.fromString(fieldName)
                ),
                treeMaker.Ident(names.fromString(fieldName))
        ));
        statements.append(aThis);
    }

    private ListBuffer<JCTree.JCVariableDecl> createParams(java.util.List<MapperFieldAST> mapperFieldASTS){
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        mapperFieldASTS.forEach(mapperFieldAST -> {
            MapperField mapperField = mapperFieldAST.getMapperField();
            JCTree.JCVariableDecl param = treeMaker.VarDef(
                    treeMaker.Modifiers(Flags.PARAMETER),
                    names.fromString(mapperField.getSimpleName()),
                    treeMaker.Ident(names.fromString(mapperField.getType().toString())),
                    null
            );
            params.append(param);
        });
        return params;
    }
}
