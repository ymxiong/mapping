package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.BaseHandler;
import cc.eamon.open.mapping.ast.handler.field.*;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:23
 */
public class MapperFieldPipeline extends BaseMapperPipeline {


    public MapperFieldPipeline(MapperInfo mapperInfo) {
        super(mapperInfo);
    }

    @Override
    public MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        addLast(new IgnoreFieldHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new RenameFieldHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new ModifyFieldHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new ExtraFieldHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new CommonFieldHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        return this.mapperInfo;
    }
}
