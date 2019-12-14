package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.enhancement.Doc;
import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.DocStrategy;
import com.squareup.javapoet.AnnotationSpec;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperTypeDocDecorator extends MapperTypeDecorator{
    public MapperTypeDocDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    public void decorate() {
        DocStrategy typeDocStrategy = (DocStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.DOC.getName());
        if (typeDocStrategy.getNote()!=null){
            AnnotationSpec annotationSpec=AnnotationSpec.builder(Doc.class)
                    .addMember("note","\""+typeDocStrategy.getNote()+"\"")
                    .build();
            typeBuilder.getTypeSpec().addAnnotation(annotationSpec);
        }
    }

}
