package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

import javax.lang.model.type.TypeMirror;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 23:41:00
 */
public class ExtraDetail implements MapperDetail {

    private String mapper;

    private String name;

    private TypeMirror type;

    private String[] typeArgs;

    private Boolean list;

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeMirror getType() {
        return type;
    }

    public void setType(TypeMirror type) {
        this.type = type;
    }

    public String[] getTypeArgs() {
        return typeArgs;
    }

    public void setTypeArgs(String[] typeArgs) {
        this.typeArgs = typeArgs;
    }

    public Boolean getList() {
        return list;
    }

    public void setList(Boolean list) {
        this.list = list;
    }
}
