package cc.eamon.open.mapping.ast.handler;


import java.util.Objects;

/**
 * @author lzr
 * @date 2021/1/20 15:54
 */
public class HandlerContext {

    private HandlerContext prev;

    private HandlerContext next;

    private AbstractHandler handler;

    public HandlerContext(){

    }

    public HandlerContext(AbstractHandler handler){
        this.handler = handler;
    }

    public void fireConvertExecuted(AST tree){
        invokeConvertExecuted(next(), tree);
    }

    private static void invokeConvertExecuted(HandlerContext ctx, AST tree){
        if(Objects.nonNull(ctx) && Objects.nonNull(ctx.handler())) {
            ctx.handler().executeConvert(ctx, tree);
        }
    }

    public HandlerContext next(){
        return this.next;
    }

    public HandlerContext prev(){
        return this.prev;
    }

    public void setPrev(HandlerContext prev){
        this.prev = prev;
    }

    public void setNext(HandlerContext next){
        this.next = next;
    }

    private AbstractHandler handler(){
        return this.handler;
    }
}
