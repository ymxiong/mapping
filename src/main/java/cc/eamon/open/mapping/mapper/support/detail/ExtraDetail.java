package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 23:41:00
 */
public class ExtraDetail implements MapperDetail {

    private String mapper;

    private String name;

    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getList() {
        return list;
    }

    public void setList(Boolean list) {
        this.list = list;
    }
}
