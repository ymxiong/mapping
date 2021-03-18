package cc.eamon.open.mapping.ast.pipeline;

import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.handler.AbstractHandler;
import cc.eamon.open.mapping.ast.handler.BaseHandler;
import cc.eamon.open.mapping.ast.item.MapperInfo;

/**
 * @author lzr
 * @date 2021/3/10 8:16
 */
public interface MapperPipeline {

    MapperPipeline invoke(AST tree);

    void addLast(BaseHandler handler);

    void addNextPipeline(BaseMapperPipeline next);
}
