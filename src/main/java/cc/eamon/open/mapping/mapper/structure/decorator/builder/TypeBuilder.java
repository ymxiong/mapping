package cc.eamon.open.mapping.mapper.structure.decorator.builder;

import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.util.HashMap;
import java.util.Map;


/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-10 10:20:38
 */
public class TypeBuilder {

    private MapperType mapperType;

    private String name;

    private TypeSpec.Builder typeSpec;

    private ClassName self;

    private Map<String, MethodSpec.Builder> BaseMethods=new HashMap<String,MethodSpec.Builder>();

    public TypeBuilder(MapperType mapperType) {
        this.mapperType = mapperType;
    }

    public MapperType getMapperType() {
        return mapperType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeSpec.Builder getTypeSpec() {
        return typeSpec;
    }

    public void setTypeSpec(TypeSpec.Builder typeSpec) {
        this.typeSpec = typeSpec;
    }

    public ClassName getSelf() {
        return self;
    }

    public void setSelf(ClassName self) {
        this.self = self;
    }

    public Map<String, MethodSpec.Builder> getBaseMethods() {
        return BaseMethods;
    }

    public void setBaseMethods(Map<String, MethodSpec.Builder> baseMethods) {
        BaseMethods = baseMethods;
    }
}
