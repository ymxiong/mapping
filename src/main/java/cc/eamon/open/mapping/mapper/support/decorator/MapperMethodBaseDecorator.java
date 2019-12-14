package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.MethodBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperMethod;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperMethodBaseDecorator extends MapperTypeDecorator {

    public MapperMethodBaseDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public void decorate() {
        for (MapperMethod method:typeBuilder.getMapperType().getMapperMethodList()) {
            MethodBuilder methodBuilder=new MethodBuilder(typeBuilder.getMapperType(),method,typeBuilder.getSelf());
        }
    }

}
