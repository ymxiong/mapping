package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.method.convert.ConvertABHandler;
import cc.eamon.open.mapping.ast.handler.method.convert.ConvertBAHandler;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/13 15:07
 */
public class ConvertMapperMethodPipeline extends BaseMapperPipeline{

    public ConvertMapperMethodPipeline(MapperInfo mapperInfo){
        super(mapperInfo);
    }

    @Override
    public MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        addLast(new ConvertABHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        addLast(new ConvertBAHandler().buildBaseInfo(this.mapperInfo, jcClassDecl, treeMaker, names));
        return this.mapperInfo;
    }
}
