package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.util.ClassUtil;
import cc.eamon.open.mapping.mapper.util.StringUtil;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import com.alibaba.fastjson.JSONObject;
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
        ExtraStrategy extraStrategy = (ExtraStrategy) type.getStrategies().get(MapperEnum.EXTRA.getName());

        if (extendsStrategy.open()) {
            typeSpec.superclass(ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // define import items
//        ClassName string = ClassName.get("java.lang", "String");
//        ClassName map = ClassName.get("java.util", "Map");
//        ClassName list = ClassName.get("java.util", "List");
//        ClassName linkedHashMap = ClassName.get("java.util", "LinkedHashMap");
//        TypeName typeOfMap = ParameterizedTypeName.get(map, string, string);

        String buildMapStaticMethod = "buildMap";
        MethodSpec.Builder buildMapStaticMethodSpec = MethodSpec.methodBuilder(buildMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(ClassUtil.getParameterizedMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildMapStaticMethodSpec.addStatement("Map<String, String> map = $T.buildMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildMapStaticMethodSpec.addStatement("Map<String, String> map = new $T<>()", ClassUtil.getLinkedHashMap());
        }
        buildMapStaticMethodSpec.addStatement("if (obj == null) return map");

        String buildEntityMethod = "buildEntity";
        MethodSpec.Builder buildEntityMethodSpec = MethodSpec.methodBuilder(buildEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(self);

        // build obj
        buildEntityMethodSpec.addStatement("$T obj = new $T()", self, self);
        if (extendsStrategy.open()) {
            buildEntityMethodSpec.addStatement("$T.copyEntity(super.buildEntity(), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }

        String parseEntityStaticMethod = "parseEntity";
        MethodSpec.Builder parseEntityStaticMethodSpec = MethodSpec.methodBuilder(parseEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassUtil.getParameterizedMap(), "map")
                .returns(self);

        // build obj
        parseEntityStaticMethodSpec.addStatement("$T obj = new $T()", self, self);
        if (extendsStrategy.open()) {
            parseEntityStaticMethodSpec.addStatement("$T.copyEntity($T.parseEntity(map), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()), ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
        parseEntityStaticMethodSpec.addStatement("if (map == null) return obj");

        // copy obj
        String copyEntityStaticMethod = "copyEntity";
        MethodSpec.Builder copyEntityStaticMethodSpec = MethodSpec.methodBuilder(copyEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "from")
                .addParameter(self, "to")
                .returns(TypeName.VOID);

        copyEntityStaticMethodSpec.addStatement("if (from == null || to == null) return");

        for (MapperField field : type.getMapperFieldList()) {

            IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());
            RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
            ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

            if ((ignoreStrategy.ignore())) {
                continue;
            }

            FieldSpec.Builder fieldSpec = FieldSpec.builder(
                    ClassUtil.get(modifyStrategy.getModifyType()),
                    renameStrategy.getName(),
                    Modifier.PUBLIC);
            typeSpec.addField(fieldSpec.build());

            buildMapStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", $T.toJSONString(" + modifyStrategy.getModifyName() + "))", JSONObject.class);
            buildEntityMethodSpec.addStatement(modifyStrategy.getRecoverName().replace("$", "this." + renameStrategy.getName()));
            parseEntityStaticMethodSpec.addStatement(modifyStrategy.getRecoverName().replace("$", "$T.parseObject(map.get(\"" + renameStrategy.getName() + "\"), $T.class)"), JSONObject.class, ClassUtil.get(modifyStrategy.getModifyType()));
            copyEntityStaticMethodSpec.addStatement("to.set" + StringUtil.firstWordToUpperCase(field.getSimpleName()) + "(from.get" + StringUtil.firstWordToUpperCase(field.getSimpleName()) + "())");
        }

        // add return
        buildMapStaticMethodSpec.addStatement("return map");
        buildEntityMethodSpec.addStatement("return obj");
        parseEntityStaticMethodSpec.addStatement("return obj");

        // add method
        typeSpec.addMethod(buildMapStaticMethodSpec.build());
        typeSpec.addMethod(buildEntityMethodSpec.build());
        typeSpec.addMethod(parseEntityStaticMethodSpec.build());
        typeSpec.addMethod(copyEntityStaticMethodSpec.build());

        if (extraStrategy.open()) {

            String buildMapExtraStaticMethod = "buildMapExtra";
            MethodSpec.Builder buildMapExtraStaticMethodSpec = MethodSpec.methodBuilder(buildMapExtraStaticMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .addParameter(self, "obj")
                    .returns(ClassUtil.getParameterizedMap());


            buildMapExtraStaticMethodSpec.addStatement("Map<String, String> map = buildMap(obj)");
            buildMapExtraStaticMethodSpec.addStatement("if (obj == null) return map");

            for (MapperField field : extraStrategy.getMapperFields()) {
                RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
                ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());


                if (field.getList()) {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            ClassUtil.getParameterizedList(ClassUtil.get(modifyStrategy.getModifyType())),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    buildMapExtraStaticMethodSpec.addParameter(ClassUtil.getParameterizedList(ClassUtil.get(field.getQualifiedTypeName())), renameStrategy.getName());
                } else {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            ClassUtil.get(modifyStrategy.getModifyType()),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    buildMapExtraStaticMethodSpec.addParameter(ClassUtil.get(field.getQualifiedTypeName()), renameStrategy.getName());
                }

                buildMapExtraStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", $T.toJSONString(" + renameStrategy.getName() + "))", JSONObject.class);

            }
            buildMapExtraStaticMethodSpec.addStatement("return map");

            typeSpec.addMethod(buildMapExtraStaticMethodSpec.build());
        }


        return typeSpec.build();

    }

}
