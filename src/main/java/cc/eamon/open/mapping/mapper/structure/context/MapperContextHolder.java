package cc.eamon.open.mapping.mapper.structure.context;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-06 00:48:38
 */
public class MapperContextHolder {

    /**
     * init Context for current thread
     */
    private static ThreadLocal<MapperContext> local = ThreadLocal.withInitial(MapperContext::new);

    public MapperContextHolder() {}

    public static MapperContext get(){
        return local.get();
    }

    public static void init(){
        if (local.get() == null){
            local.set(new MapperContext());
        }
    }

    public static void clear(){
        local.remove();
    }


}
