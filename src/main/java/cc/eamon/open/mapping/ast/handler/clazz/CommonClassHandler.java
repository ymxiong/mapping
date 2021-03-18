package cc.eamon.open.mapping.ast.handler.clazz;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

/**
 * @author lzr
 * @date 2021/3/5 14:44
 */
public class CommonClassHandler extends ClassHandler {

    @Override
    public JCTree buildASTClass(AST tree) {

        MapperType mapperType = this.mapperInfo.getMapperType();
        String mapperName = mapperType.getMapperName();
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) mapperType.getStrategies().get(MapperEnum.MAPPER.getName());

        JCTree.JCClassDecl jcClassDecl = this.treeMaker.ClassDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC),
                this.names.fromString(StringUtils.firstWordToUpperCase(basicMapperStrategy.getMapper())),
                List.nil(),
                null,
                List.nil(),
                getClassContent(tree, mapperName).toList()
        );
        return jcClassDecl;
    }

    @Override
    public ListBuffer<JCTree> getClassContent(AST tree, String mapperName) {

        ListBuffer<JCTree> fieldTrees = tree.getFieldTree().get(mapperName);
        ListBuffer<JCTree> methodTrees = tree.getMethodTree().get(mapperName);
        ListBuffer<JCTree> classContent = new ListBuffer<>();
        classContent.appendList(fieldTrees).appendList(methodTrees);

        return classContent;
    }

    @Override
    public void preHandle(AST tree) {

    }
}
