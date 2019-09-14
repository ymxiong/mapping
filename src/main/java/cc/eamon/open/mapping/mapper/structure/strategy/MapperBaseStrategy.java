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

    private TypeMirror elementType;

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
    public TypeMirror getElementType() {
        return elementType;
    }

    @Override
    public void setElementType(TypeMirror elementType) {
        this.elementType = elementType;
    }
}
