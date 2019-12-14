package cc.eamon.open.mapping.mapper.structure.decorator.builder;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.type.TypeMirror;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-10 10:20:38
 */
public class FieldBuilder {
    private  MapperField mapperField;

    private  FieldSpec.Builder fieldSpec;

    private  TypeName type;

    private  String name;

    private  String modifyName;

    private  TypeMirror modifyType;

    private  String recoverName;

    private TypeMirror recoverType;

    private  ClassName self;

    private  Boolean ignore;


    public FieldBuilder(MapperField mapperField) {
        this.mapperField = mapperField;
        this.ignore=false;
    }

    public MapperField getMapperField() {
        return mapperField;
    }

    public FieldSpec.Builder getFieldSpec() {
        return fieldSpec;
    }

    public void setFieldSpec(FieldSpec.Builder fieldSpec) {
        this.fieldSpec = fieldSpec;
    }

    public TypeName getType() {
        return type;
    }

    public void setType(TypeName type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClassName getSelf() {
        return self;
    }

    public void setSelf(ClassName self) {
        this.self = self;
    }

    public Boolean getIgnore() {
        return ignore;
    }

    public void setIgnore(Boolean ignore) {
        this.ignore = ignore;
    }

    public String getModifyName() {
        return modifyName;
    }

    public void setModifyName(String modifyName) {
        this.modifyName = modifyName;
    }

    public String getRecoverName() {
        return recoverName;
    }

    public void setRecoverName(String recoverName) {
        this.recoverName = recoverName;
    }

    public TypeMirror getModifyType() {
        return modifyType;
    }

    public void setModifyType(TypeMirror modifyType) {
        this.modifyType = modifyType;
    }

    public TypeMirror getRecoverType() {
        return recoverType;
    }

    public void setRecoverType(TypeMirror recoverType) {
        this.recoverType = recoverType;
    }
}
