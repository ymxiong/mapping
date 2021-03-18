package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.method.mapper.*;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:31
 */
public class CommonMapperMethodPipeline extends BaseMapperPipeline {


    public CommonMapperMethodPipeline(MapperInfo mapperInfo) {
        super(mapperInfo);
    }

    @Override
    public MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        addLast(new BuildEntityHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new BuildMapperStaticHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new BuildMapStaticHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new BuildSerialMapHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new ParseEntityHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new ParseSerialEntityHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        return this.mapperInfo;
    }
}
