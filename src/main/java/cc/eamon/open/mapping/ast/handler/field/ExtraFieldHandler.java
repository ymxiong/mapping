package cc.eamon.open.mapping.ast.handler.field;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import java.util.List;

/**
 * @author lzr
 * @date 2021/3/6 13:39
 */
public class ExtraFieldHandler extends FieldHandler {
    @Override
    public ListBuffer<JCTree> buildASTField() {
        List<MapperFieldAST> mapperFields = this.mapperInfo.getMapperFields();
        ListBuffer<JCTree> jcTrees = new ListBuffer<>();
        mapperFields.stream().filter(MapperFieldAST::getExtra).forEach(mapperFieldAST -> {
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
                JCTree.JCTypeApply list = treeMaker.TypeApply(treeMaker.Ident(names.fromString("List")), com.sun.tools.javac.util.List.of(type));
                JCTree.JCVariableDecl jcVariableDecl = this.treeMaker.VarDef(
                    this.treeMaker.Modifiers(Flags.PUBLIC),
                    names.fromString(StringUtils.firstWordToLowerCase(mapperField.getSimpleName())),
                    list,
                    mapperField.getDefaultValue().equals("null")?null:this.treeMaker.Ident(names.fromString(mapperField.getDefaultValue()))
                );
                mapperFieldAST.setAdded(true);
                jcTrees.append(jcVariableDecl);
            }
        });
        return jcTrees;
    }

    @Override
    public void preHandle(AST tree) {
        ExtraStrategy extraStrategy = (ExtraStrategy) this.mapperInfo.getMapperType().getStrategies().get(MapperEnum.EXTRA.getName());
        if(extraStrategy.open()){
            List<MapperField> mapperFields = extraStrategy.getMapperFields();
            List<MapperFieldAST> fieldASTS = MapperFieldAST.buildFieldAST(mapperFields);
            fieldASTS.forEach(mapperFieldAST -> {
                mapperFieldAST.setExtra(true);
            });
            this.mapperInfo.getMapperFields().addAll(fieldASTS);
        }
    }

}
