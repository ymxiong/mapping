package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.enhancement.Doc;
import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperFieldDecorator;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.DocStrategy;
import com.squareup.javapoet.AnnotationSpec;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperFieldDocDecorator extends MapperFieldDecorator {
    public MapperFieldDocDecorator(FieldBuilder fieldBuilder) {
        super(fieldBuilder);
    }

    @Override
    public  void  decorate() {
        DocStrategy fieldDocStrategy = (DocStrategy) fieldBuilder.getMapperField().getStrategies().get(MapperEnum.DOC.getName());
        if (fieldDocStrategy.getNote()!=null){
            AnnotationSpec annotationSpec=AnnotationSpec.builder(Doc.class)
                    .addMember(" note","\""+fieldDocStrategy.getNote()+"\"")
                    .build();
            fieldBuilder.getFieldSpec().addAnnotation(annotationSpec);
        }
    }
}
