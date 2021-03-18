package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.handler.AbstractHandler;
import cc.eamon.open.mapping.ast.handler.BaseHandler;
import cc.eamon.open.mapping.ast.handler.HandlerContext;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

/**
 * @author lzr
 * @date 2021/3/10 8:18
 */
public abstract class BaseMapperPipeline implements MapperPipeline {

    private final HandlerContext head = new HandlerContext();

    private final HandlerContext tail = new HandlerContext();

    private BaseMapperPipeline next;

    protected MapperInfo mapperInfo;

    {
        head.setNext(tail);
        tail.setPrev(head);
    }

    public BaseMapperPipeline(MapperInfo mapperInfo){
        this.mapperInfo = mapperInfo;
    }


    @Override
    public void addNextPipeline(BaseMapperPipeline next) {
        this.next = next;
    }

    @Override
    public MapperPipeline invoke(AST tree) {
        head.fireConvertExecuted(tree);
        return this;
    }

    @Override
    public void addLast(BaseHandler handler){
        HandlerContext ctx = new HandlerContext(handler);
        ctx.setNext(tail);
        ctx.setPrev(tail.prev());
        tail.prev().setNext(ctx);
        tail.setPrev(ctx);
    }

    public abstract MapperInfo buildPipeline(JCTree.JCClassDecl jcClassDecl, TreeMaker treeMaker, Names names);
}
