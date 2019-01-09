package cc.eamon.open.mapping.mapper.processor;

import cc.eamon.open.mapping.group.Group;
import cc.eamon.open.mapping.mapper.MapperGroup;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.TypeSpec;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2018-12-25 17:21:40
 */
public class MapGroupProcessor {

    public static void buildMapGroup(
            MapperGroup mapperGroup,
            String mapperName,
            TypeSpec.Builder typeSpec
    ){
        int index = 0;
        for (int i = 0; i < mapperGroup.target().length; i++) {
            if (mapperName.equals(mapperGroup.target()[i])) {

                index = i;
                AnnotationSpec.Builder annotationSpec = AnnotationSpec.builder(Group.class);
                String baseFormat = "";
                if (mapperGroup.base().length > 0 && mapperGroup.base().length <= index) {
                    baseFormat = mapperGroup.base()[mapperGroup.base().length - 1] + "";
                    annotationSpec.addMember("base", baseFormat);
                } else if (mapperGroup.base().length > 0) {
                    baseFormat = mapperGroup.base()[index] + "";
                    annotationSpec.addMember("base", baseFormat);
                }

                String valueFormat = "";
                if (mapperGroup.value().length <= index) {
                    valueFormat = "\"" + mapperGroup.value()[mapperGroup.value().length - 1] + "\"";
                    annotationSpec.addMember("value", valueFormat);
                } else {
                    valueFormat = "\"" + mapperGroup.value()[index] + "\"";
                    annotationSpec.addMember("value", valueFormat);
                }

                String nameFormat = "";
                if (mapperGroup.name().length > 0 && mapperGroup.name().length <= index) {
                    nameFormat = "\"" + mapperGroup.name()[mapperGroup.name().length - 1] + "\"";
                    annotationSpec.addMember("name", nameFormat);
                } else if (mapperGroup.name().length > 0) {
                    nameFormat = "\"" + mapperGroup.name()[index] + "\"";
                    annotationSpec.addMember("name", nameFormat);
                }

                String listFormat = "";
                if (mapperGroup.list().length > 0 && mapperGroup.list().length <= index) {
                    listFormat = mapperGroup.list()[mapperGroup.list().length - 1] + "";
                    annotationSpec.addMember("list", listFormat);
                } else if (mapperGroup.list().length > 0) {
                    listFormat = mapperGroup.list()[index] + "";
                    annotationSpec.addMember("list", listFormat);
                }
                typeSpec.addAnnotation(annotationSpec.build());
            }
        }
    }
}
