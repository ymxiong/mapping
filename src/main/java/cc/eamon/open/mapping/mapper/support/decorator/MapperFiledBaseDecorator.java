package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.FieldBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperFiledBaseDecorator extends MapperTypeDecorator {

    public MapperFiledBaseDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public void decorate() {
        for (MapperField mapperField : typeBuilder.getMapperType().getMapperFieldList()) {
            FieldBuilder fieldBuilder = new FieldBuilder(mapperField);
            //首先处理@MapperIgnore注解
            MapperIgnoreDecorator mapperIgnoreDecorator = new MapperIgnoreDecorator(fieldBuilder);
            mapperIgnoreDecorator.decorate();

            if (fieldBuilder.getIgnore()) {
                continue;
            }

            //处理MapperRename注解
            MapperRenameDecorator mapperRenameDecorator = new MapperRenameDecorator(fieldBuilder);
            mapperRenameDecorator.decorate();
            //处理MapperModify注解
            MapperModifyDecorator mapperModifyDecorator = new MapperModifyDecorator(fieldBuilder);
            mapperModifyDecorator.decorate();

            fieldBuilder.setFieldSpec(FieldSpec.builder(
                    fieldBuilder.getType(),
                    fieldBuilder.getName(),
                    Modifier.PUBLIC));
            //处理MapperDoc注解
            MapperFieldDocDecorator mapperTypeDocDecorator = new MapperFieldDocDecorator(fieldBuilder);
            mapperTypeDocDecorator.decorate();

            typeBuilder.getTypeSpec().addField(fieldBuilder.getFieldSpec().build());


            typeBuilder.getBaseMethods().get("buildMap").addStatement("map.put(\"" + fieldBuilder.getName() + "\", " + fieldBuilder.getModifyName() + ")");
            typeBuilder.getBaseMethods().get("buildSerialMap").addStatement("map.put(\"" + fieldBuilder.getName() + "\", $T.toJSONString(" + fieldBuilder.getModifyName() + "))", JSONObject.class);
            typeBuilder.getBaseMethods().get("buildEntity").addStatement(fieldBuilder.getRecoverName().replace("$", "this." + fieldBuilder.getName()));
            typeBuilder.getBaseMethods().get("parseEntity").addStatement(fieldBuilder.getRecoverName().replace("$", "($T) map.get(\"" + fieldBuilder.getName() + "\")"),
                    fieldBuilder.getModifyType());
            if (MapperUtils.loadTypeArguments(fieldBuilder.getModifyType()).size() > 0) {
                typeBuilder.getBaseMethods().get("parseSerialEntity").addStatement(fieldBuilder.getRecoverName().replace("$", "$T.parseObject(map.get(\"" + fieldBuilder.getName() + "\"), new $T<$T>(){})"),
                        JSONObject.class, TypeReference.class, fieldBuilder.getModifyType());
            } else {
                typeBuilder.getBaseMethods().get("parseSerialEntity").addStatement(fieldBuilder.getRecoverName().replace("$", "$T.parseObject(map.get(\"" + fieldBuilder.getName() + "\"), $T.class)"),
                        JSONObject.class, fieldBuilder.getModifyType());
            }
            typeBuilder.getBaseMethods().get("copyEntity").addStatement("to.set" + StringUtils.firstWordToUpperCase(mapperField.getSimpleName()) + "(from.get" + StringUtils.firstWordToUpperCase(mapperField.getSimpleName()) + "())");

        }
    }
}

