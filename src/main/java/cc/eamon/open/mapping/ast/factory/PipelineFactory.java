package cc.eamon.open.mapping.ast.factory;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:45
 */
public interface PipelineFactory {

    void buildPipeline(AST tree, MapperInfo mapperInfo, JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names);

}
