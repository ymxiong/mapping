package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.StringUtil;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.IgnoreStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 18:21:02
 */
public class MapperBuilder {

    public static TypeSpec build(MapperType type) {
        // define new type
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(type.getSimpleName() + StringUtil.firstWordToUpperCase(type.getMapperName()) + "Mapper");
        typeSpec.addModifiers(Modifier.PUBLIC);

        // type strategies
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) type.getStrategies().get(MapperEnum.EXTENDS.getName());
        if (extendsStrategy.open()) {
            typeSpec.superclass(ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // define import items
        ClassName string = ClassName.get("java.lang", "String");
        ClassName object = ClassName.get("java.lang", "Object");
        ClassName map = ClassName.get("java.util", "Map");
        ClassName linkedHashMap = ClassName.get("java.util", "LinkedHashMap");
        TypeName typeOfMap = ParameterizedTypeName.get(map, string, object);

        String buildMapStaticMethod = "buildMap";
        MethodSpec.Builder buildMapStaticMethodSpec = MethodSpec.methodBuilder(buildMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(typeOfMap);

        // build resultMap
        if (extendsStrategy.open()) {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = $T.buildMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = new $T<>()", linkedHashMap);
        }
        buildMapStaticMethodSpec.addStatement("if (obj == null) return map");

        String buildEntityMethod = "buildEntity";
        MethodSpec.Builder buildEntityMethodSpec = MethodSpec.methodBuilder(buildEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(self);

        // build obj
        if (extendsStrategy.open()) {
            buildEntityMethodSpec.addStatement("$T obj = ($T) super.buildEntity()", self, self);
        } else {
            buildEntityMethodSpec.addStatement("$T obj = new $T()", self, self);
        }

        String parseEntityStaticMethod = "parseEntity";
        MethodSpec.Builder parseEntityStaticMethodSpec = MethodSpec.methodBuilder(parseEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(typeOfMap, "map")
                .returns(self);

        // build obj
        if (extendsStrategy.open()) {
            parseEntityStaticMethodSpec.addStatement("$T obj = ($T) $T.parseEntity(map)", self, self, ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            parseEntityStaticMethodSpec.addStatement("$T obj = new $T()", self, self);
        }
        parseEntityStaticMethodSpec.addStatement("if (map == null) return obj");


        for (MapperField field : type.getMapperFieldList()) {

            IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());
            RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
            ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

            if ((ignoreStrategy.ignore())) {
                continue;
            }

            FieldSpec.Builder fieldSpec = FieldSpec.builder(
                    TypeName.get(modifyStrategy.getModifyType()),
                    renameStrategy.getName(),
                    Modifier.PUBLIC);
            typeSpec.addField(fieldSpec.build());

            buildMapStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", " + modifyStrategy.getModifyName() + ")");
            buildEntityMethodSpec.addStatement(modifyStrategy.getRecoverName().replace("$", "this." + renameStrategy.getName()));
            parseEntityStaticMethodSpec.addStatement(modifyStrategy.getRecoverName().replace("$", "($T)map.get(\"" + renameStrategy.getName() + "\")"), modifyStrategy.getModifyType());
        }

        // add return
        buildMapStaticMethodSpec.addStatement("return map");
        buildEntityMethodSpec.addStatement("return obj");
        parseEntityStaticMethodSpec.addStatement("return obj");

        // add method
        typeSpec.addMethod(buildMapStaticMethodSpec.build());
        typeSpec.addMethod(buildEntityMethodSpec.build());
        typeSpec.addMethod(parseEntityStaticMethodSpec.build());
        return typeSpec.build();

    }

}
