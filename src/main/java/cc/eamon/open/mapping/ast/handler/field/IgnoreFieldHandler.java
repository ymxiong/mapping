package cc.eamon.open.mapping.ast.handler.field;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.IgnoreStrategy;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzr
 * @date 2021/3/6 14:41
 */
public class IgnoreFieldHandler extends FieldHandler {
    @Override
    public ListBuffer<JCTree> buildASTField() {
        return null;
    }

    @Override
    public void preHandle(AST tree) {
        List<MapperField> mapperFieldList = this.mapperInfo.getMapperType().getMapperFieldList();
        List<MapperFieldAST> newMapperFields = new ArrayList<>();
        mapperFieldList.forEach(mapperField -> {
            IgnoreStrategy ignoreStrategy = (IgnoreStrategy) mapperField.getStrategies().get(MapperEnum.IGNORE.getName());
            if(!ignoreStrategy.ignore()){
                newMapperFields.add(new MapperFieldAST(mapperField));
            }
        });

        this.mapperInfo.setMapperFields(newMapperFields);
    }
}
