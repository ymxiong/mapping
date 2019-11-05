package cc.eamon.open.mapping.mapper.structure.strategy;

import javax.lang.model.type.TypeMirror;

/**
 * basic info for value strategy
 *
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 13:41:57
 */
public abstract class MapperBaseStrategy implements MapperStrategy {

    private String mapper;

    private String elementName;

    private TypeMirror type;

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

    @Override
    public TypeMirror getType() {
        return type;
    }

    @Override
    public void setType(TypeMirror type) {
        this.type = type;
    }
}
