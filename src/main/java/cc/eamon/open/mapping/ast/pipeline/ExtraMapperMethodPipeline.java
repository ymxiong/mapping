package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.method.extra.BuildMapExtraHandler;
import cc.eamon.open.mapping.ast.handler.method.extra.BuildMapHandler;
import cc.eamon.open.mapping.ast.handler.method.extra.BuildSerialMapExtraHandler;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:34
 */
public class ExtraMapperMethodPipeline extends BaseMapperPipeline {


    public ExtraMapperMethodPipeline(MapperInfo mapperInfo) {
        super(mapperInfo);
    }

    @Override
    public MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        addLast(new BuildMapExtraHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new BuildMapHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new BuildSerialMapExtraHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        return this.mapperInfo;
    }
}
