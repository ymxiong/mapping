package cc.eamon.open.mapping.mapper.structure.detail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 18:45:01
 */
public class RenameDetail implements MapperDetail {

    private String mapper;

    private String elementName;

    private String originName;

    private String freshName;

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getOriginName() {
        return originName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getFreshName() {
        return freshName;
    }

    public void setFreshName(String freshName) {
        this.freshName = freshName;
    }
}
