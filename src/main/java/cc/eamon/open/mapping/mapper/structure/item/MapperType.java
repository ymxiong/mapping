package cc.eamon.open.mapping.mapper.structure.item;

import cc.eamon.open.mapping.mapper.StringUtil;
import cc.eamon.open.mapping.mapper.structure.factory.support.MapperEnum;
import cc.eamon.open.mapping.mapper.structure.strategy.ignore.IgnoreStrategy;
import cc.eamon.open.mapping.mapper.structure.strategy.rename.RenameStrategy;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-14 11:32:56
 */
public class MapperType {

    private String packageName;

    private String simpleName;

    private String qualifiedName;

    private String mapperName;

    private List<MapperMethod> mapperMethodList;

    private List<MapperField> mapperFieldList;

    private MapperType() {}

    private MapperType(String packageName, String simpleName, String qualifiedName, String mapperName, List<MapperMethod> mapperMethodList, List<MapperField> mapperFieldList) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.qualifiedName = qualifiedName;
        this.mapperName = mapperName;
        this.mapperMethodList = mapperMethodList;
        this.mapperFieldList = mapperFieldList;
    }

    public TypeSpec generate() {
        // 新建类
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(simpleName + StringUtil.firstWordToUpperCase(mapperName) + "Mapper");
        typeSpec.addModifiers(Modifier.PUBLIC);

        ClassName self = ClassName.get(packageName, simpleName);
        // 生成getMap方法
        // 确定需要import的项
        ClassName string = ClassName.get("java.lang", "String");
        ClassName object = ClassName.get("java.lang", "Object");
        ClassName map = ClassName.get("java.util", "Map");
        ClassName linkedHashMap = ClassName.get("java.util", "LinkedHashMap");
        TypeName typeOfMap = ParameterizedTypeName.get(map, string, object);

        String buildMapMethod = "buildMap";
        MethodSpec.Builder buildMapMethodSpec = MethodSpec.methodBuilder(buildMapMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(typeOfMap);


        // 创建resultMap
        buildMapMethodSpec.addStatement("Map<String, Object> map = new $T<>()", linkedHashMap);
        buildMapMethodSpec.addStatement("if (obj == null) return map");

        String loadEntityMethod = "loadEntity";
        MethodSpec.Builder loadEntityMethodSpec = MethodSpec.methodBuilder(loadEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(typeOfMap, "map")
                .returns(self);

        // 创建obj
        loadEntityMethodSpec.addStatement("$T obj = new $T()", self, self);
        loadEntityMethodSpec.addStatement("if (map == null) return obj");

        String buildEntityMethod = "buildEntity";
        MethodSpec.Builder buildEntityMethodSpec = MethodSpec.methodBuilder(buildEntityMethod)
                .addModifiers(Modifier.PUBLIC)
                .returns(self);

        // 创建obj
        buildEntityMethodSpec.addStatement("$T entity = new $T()", self, self);

        for (MapperField field : mapperFieldList) {

            IgnoreStrategy ignoreStrategy = (IgnoreStrategy)field.getStrategies().get(MapperEnum.IGNORE.getName());
            RenameStrategy renameStrategy = (RenameStrategy)field.getStrategies().get(MapperEnum.RENAME.getName());

            if ((ignoreStrategy.ignore())){
                continue;
            }

            FieldSpec.Builder fieldSpec = FieldSpec.builder(
                    TypeName.get(field.getFieldType()),
                    renameStrategy.getName(),
                    Modifier.PUBLIC);
            typeSpec.addField(fieldSpec.build());

            buildMapMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", " + "obj.get" + StringUtil.firstWordToUpperCase(field.getSimpleName()) + "())");
            loadEntityMethodSpec.addStatement("obj.set" + StringUtil.firstWordToUpperCase(field.getSimpleName()) + "(($T)map.get(\"" + renameStrategy.getName() + "\"))", field.getFieldType());
            buildEntityMethodSpec.addStatement("entity.set" + StringUtil.firstWordToUpperCase(field.getSimpleName()) + "(this." + renameStrategy.getName() + ")");
        }

        // 添加返回结果
        buildMapMethodSpec.addStatement("return map");
        loadEntityMethodSpec.addStatement("return obj");
        buildEntityMethodSpec.addStatement("return entity");


        typeSpec.addMethod(buildMapMethodSpec.build());
        typeSpec.addMethod(loadEntityMethodSpec.build());
        typeSpec.addMethod(buildEntityMethodSpec.build());
        return typeSpec.build();
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public List<MapperMethod> getMapperMethodList() {
        return mapperMethodList;
    }

    public void setMapperMethodList(List<MapperMethod> mapperMethodList) {
        this.mapperMethodList = mapperMethodList;
    }

    public List<MapperField> getMapperFieldList() {
        return mapperFieldList;
    }

    public void setMapperFieldList(List<MapperField> mapperFieldList) {
        this.mapperFieldList = mapperFieldList;
    }


    public static MapperType.MapperTypeBuilder builder(){
        return new MapperType.MapperTypeBuilder();
    }

    public static class MapperTypeBuilder{

        private String packageName;

        private String simpleName;

        private String qualifiedName;

        private String mapperName;

        private List<MapperMethod> mapperMethodList;

        private List<MapperField> mapperFieldList;

        private MapperTypeBuilder(){}

        public MapperTypeBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public MapperTypeBuilder simpleName(String simpleName) {
            this.simpleName = simpleName;
            return this;
        }

        public MapperTypeBuilder qualifiedName(String qualifiedName) {
            this.qualifiedName = qualifiedName;
            return this;
        }

        public MapperTypeBuilder mapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        public MapperTypeBuilder mapperMethodList(List<MapperMethod> mapperMethodList) {
            this.mapperMethodList = mapperMethodList;
            return this;
        }

        public MapperTypeBuilder mapperFieldList(List<MapperField> mapperFieldList) {
            this.mapperFieldList = mapperFieldList;
            return this;
        }

        public MapperType build(){
            return new MapperType(packageName, simpleName, qualifiedName, mapperName, mapperMethodList, mapperFieldList);
        }
    }

}