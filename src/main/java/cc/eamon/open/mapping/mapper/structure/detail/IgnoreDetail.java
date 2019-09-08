package cc.eamon.open.mapping.mapper.structure.detail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-07 16:59:21
 */
public class IgnoreDetail implements MapperDetail {

    private String mapper;

    private String elementName;

    @Override
    public String getMapper() {
        return mapper;
    }

    @Override
    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    @Override
    public String getElementName() {
        return elementName;
    }

    @Override
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
