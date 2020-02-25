package cc.eamon.open.mapping.mapper.structure.strategy;

import javax.lang.model.type.TypeMirror;

/**
 * strategy of value running logic
 * <p>
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-08 19:10:25
 */
public interface MapperStrategy {

    /**
     * get target value name
     * @return String
     */
    String getMapper();

    /**
     * set target value name
     * @param mapper name
     */
    void setMapper(String mapper);

    /**
     * get element name
     * @return element name
     */
    String getElementName();

    /**
     * set element name
     * @param elementName element name
     */
    void setElementName(String elementName);


    /**
     * get element type
     * @return element type
     */
    TypeMirror getType();

    /**
     * set element type
     * @param type of element
     */
    void setType(TypeMirror type);

    /**
     * get element type args
     * @return element typeArgs
     */
    String[] getTypeArgs();

    /**
     * set element typeArgs
     * @param typeArgs of element
     */
    void setTypeArgs(String[] typeArgs);

}
