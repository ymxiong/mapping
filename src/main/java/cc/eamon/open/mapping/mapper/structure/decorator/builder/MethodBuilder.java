package cc.eamon.open.mapping.mapper.structure.decorator.builder;

import cc.eamon.open.mapping.mapper.structure.item.MapperMethod;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;


/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-10 10:20:38
 */
public class MethodBuilder {
    private MapperType mapperType;

    private MapperMethod mapperField;

    private MethodSpec.Builder methodSpec;

    private TypeName returnType;

    private ClassName self;

    public MethodBuilder(MapperType mapperType, MapperMethod mapperField,ClassName self) {
        this.mapperType = mapperType;
        this.mapperField = mapperField;
        this.self=self;
    }

    public MapperType getMapperType() {
        return mapperType;
    }

    public MapperMethod getMapperField() {
        return mapperField;
    }

    public MethodSpec.Builder getMethodSpec() {
        return methodSpec;
    }

    public void setMethodSpec(MethodSpec.Builder methodSpec) {
        this.methodSpec = methodSpec;
    }

    public TypeName getReturnType() {
        return returnType;
    }

    public void setReturnType(TypeName returnType) {
        this.returnType = returnType;
    }

    public ClassName getSelf() {
        return self;
    }

    public void setSelf(ClassName self) {
        this.self = self;
    }
}
