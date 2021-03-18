package cc.eamon.open.mapping.ast.factory;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.ast.pipeline.*;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraEnableStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraStrategy;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:47
 */
public class DefaultPipelineFactory implements PipelineFactory {


    @Override
    public void buildPipeline(AST tree, MapperInfo mapperInfo, JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names) {
        BaseMapperPipeline pipeline = new MapperFieldPipeline(mapperInfo);
        mapperInfo = pipeline.buildPipeline(jcClassDecl, treeMaker, names);
        pipeline.invoke(tree);
        pipeline = new CommonMapperMethodPipeline(mapperInfo);
        mapperInfo = pipeline.buildPipeline(jcClassDecl, treeMaker, names);
        pipeline.invoke(tree);
        ExtraStrategy extraStrategy = (ExtraStrategy) mapperInfo.getMapperType().getStrategies().get(MapperEnum.EXTRA.getName());
        if (extraStrategy instanceof ExtraEnableStrategy) {
            pipeline = new ExtraMapperMethodPipeline(mapperInfo);
            mapperInfo = pipeline.buildPipeline(jcClassDecl, treeMaker, names);
            pipeline.invoke(tree);
        }
        ConvertStrategy convertStrategy = (ConvertStrategy) mapperInfo.getMapperType().getStrategies().get(MapperEnum.CONVERT.getName());
        if (convertStrategy.open()) {
            pipeline = new ConvertMapperMethodPipeline(mapperInfo);
            mapperInfo = pipeline.buildPipeline(jcClassDecl, treeMaker, names);
            pipeline.invoke(tree);
        }
        pipeline = new MapperClassPipeline(mapperInfo);
        mapperInfo = pipeline.buildPipeline(jcClassDecl, treeMaker, names);
        pipeline.invoke(tree);
    }

}
