package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtendsStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.Map;


/**
 * Author:Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperBaseMethodDecorator  extends MapperTypeDecorator {

    public MapperBaseMethodDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    //
    public void decorate(){
        builMap();
        buildSerialMap();
        buildEntity();
        parseEntity();
        parseSerialEntity();
        copyEntity();
    };


    //buildMap
    public void builMap(){
        logger.info("Mapping build init buildMap:" + typeBuilder.getMapperType().getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        String buildMapStaticMethod = "buildMap";
        MethodSpec.Builder buildMapStaticMethodSpec = MethodSpec.methodBuilder(buildMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(typeBuilder.getSelf(), "obj")
                .returns(ClassUtils.getParameterizedObjectMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = $T.buildMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildMapStaticMethodSpec.addStatement("Map<String, Object> map = new $T<>()", ClassUtils.getLinkedHashMap());
        }
        buildMapStaticMethodSpec.addStatement("if (obj == null) return map");
        typeBuilder.getBaseMethods().put("buildMap",buildMapStaticMethodSpec);
    }


    //buildSerialMap
    public void buildSerialMap(){
        // init: build serial map
        logger.info("Mapping build init buildSerialMap:" + typeBuilder.getMapperType().getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        String buildSerialMapStaticMethod = "buildSerialMap";
        MethodSpec.Builder buildSerialMapStaticMethodSpec = MethodSpec.methodBuilder(buildSerialMapStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(typeBuilder.getSelf(), "obj")
                .returns(ClassUtils.getParameterizedStringMap());

        // build resultMap
        if (extendsStrategy.open()) {
            buildSerialMapStaticMethodSpec.addStatement("Map<String, String> map = $T.buildSerialMap(obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        } else {
            buildSerialMapStaticMethodSpec.addStatement("Map<String, String> map = new $T<>()", ClassUtils.getLinkedHashMap());
        }
        typeBuilder.getBaseMethods().put("buildSerialMap",buildSerialMapStaticMethodSpec);

    }
    //buildEntity
    public void buildEntity(){
        // init: build entity
        logger.info("Mapping build init buildEntity:" + typeBuilder.getMapperType().getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        String buildEntityMethod = "buildEntity";
        MethodSpec.Builder buildEntityMethodSpec = MethodSpec.methodBuilder(buildEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(typeBuilder.getSelf());

        // build obj
        buildEntityMethodSpec.addStatement("$T obj = new $T()", typeBuilder.getSelf(), typeBuilder.getSelf());
        if (extendsStrategy.open()) {
            buildEntityMethodSpec.addStatement("$T.copyEntity(super.buildEntity(), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }


        typeBuilder.getBaseMethods().put("buildEntity",buildEntityMethodSpec);

    }
    //parseEntity
    public void parseEntity(){

        // init: parse entity
        logger.info("Mapping build init parseEntity:" + typeBuilder.getMapperType().getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        String parseEntityStaticMethod = "parseEntity";
        MethodSpec.Builder parseEntityStaticMethodSpec = MethodSpec.methodBuilder(parseEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassUtils.getParameterizedObjectMap(), "map")
                .returns(typeBuilder.getSelf());

        // build obj
        parseEntityStaticMethodSpec.addStatement("$T obj = new $T()", typeBuilder.getSelf(), typeBuilder.getSelf());
        if (extendsStrategy.open()) {
            parseEntityStaticMethodSpec.addStatement("$T.copyEntity($T.parseEntity(map), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()), ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
        parseEntityStaticMethodSpec.addStatement("if (map == null) return obj");

        typeBuilder.getBaseMethods().put("parseEntity",parseEntityStaticMethodSpec);



    }
    //parseSerialEntity
    public void parseSerialEntity(){
        // init: parse entity
        logger.info("Mapping build init parseEntity:" + typeBuilder.getMapperType().getQualifiedName());
        ExtendsStrategy extendsStrategy = (ExtendsStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.EXTENDS.getName());
        String parseSerialEntityStaticMethod = "parseSerialEntity";
        MethodSpec.Builder parseSerialEntityStaticMethodSpec = MethodSpec.methodBuilder(parseSerialEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(ClassUtils.getParameterizedStringMap(), "map")
                .returns(typeBuilder.getSelf());

        // build obj
        parseSerialEntityStaticMethodSpec.addStatement("$T obj = new $T()", typeBuilder.getSelf(), typeBuilder.getSelf());
        if (extendsStrategy.open()) {
            parseSerialEntityStaticMethodSpec.addStatement("$T.copyEntity($T.parseEntity(map), obj)", ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()), ClassName.get(extendsStrategy.getPackageName(), extendsStrategy.getSuperMapperName()));
        }
        parseSerialEntityStaticMethodSpec.addStatement("if (map == null) return obj");

        typeBuilder.getBaseMethods().put("parseSerialEntity",parseSerialEntityStaticMethodSpec);

    }
    //copyEntity
    public void copyEntity(){
        // init: copy entity
        logger.info("Mapping build init copyEntity:" + typeBuilder.getMapperType().getQualifiedName());
        String copyEntityStaticMethod = "copyEntity";
        MethodSpec.Builder copyEntityStaticMethodSpec = MethodSpec.methodBuilder(copyEntityStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(typeBuilder.getSelf(), "from")
                .addParameter(typeBuilder.getSelf(), "to")
                .returns(TypeName.VOID);

        copyEntityStaticMethodSpec.addStatement("if (from == null || to == null) return");


        typeBuilder.getBaseMethods().put("copyEntity",copyEntityStaticMethodSpec);


    }

    public void addReturn(){
        typeBuilder.getBaseMethods().get("buildMap").addStatement("return map");
        typeBuilder.getBaseMethods().get("buildSerialMap").addStatement("return map");
        typeBuilder.getBaseMethods().get("buildEntity").addStatement("return obj");
        typeBuilder.getBaseMethods().get("parseEntity").addStatement("return obj");
        typeBuilder.getBaseMethods().get("parseSerialEntity").addStatement("return obj");
    }

    public void addMethodToType(){
        for (Map.Entry<String, MethodSpec.Builder> entry : typeBuilder.getBaseMethods().entrySet()) {
            typeBuilder.getTypeSpec().addMethod( entry.getValue().build());
        }
    }


}
