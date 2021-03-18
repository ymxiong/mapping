package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.clazz.CommonClassHandler;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:43
 */
public class MapperClassPipeline extends BaseMapperPipeline {


    public MapperClassPipeline(MapperInfo mapperInfo) {
        super(mapperInfo);
    }

    @Override
    public MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        addLast(new CommonClassHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        return this.mapperInfo;
    }
}
