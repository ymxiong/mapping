package cc.eamon.open.mapping.ast.handler.field;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperFieldAST;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import java.util.List;

/**
 * @author lzr
 * @date 2021/3/6 13:52
 */
public class ModifyFieldHandler extends FieldHandler {
    @Override
    public ListBuffer<JCTree> buildASTField() {
        return null;
    }

    @Override
    public void preHandle(AST tree) {
        List<MapperFieldAST> mapperFields = this.mapperInfo.getMapperFields();
        mapperFields.forEach(mapperFieldAST -> {
            ModifyStrategy modifyStrategy = (ModifyStrategy) mapperFieldAST.getMapperField().getStrategies().get(MapperEnum.MODIFY.getName());
            mapperFieldAST.getMapperField().setType(modifyStrategy.getModifyType());
        });
    }
}
