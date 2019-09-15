package cc.eamon.open.mapping.mapper.structure.strategy;

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
    String getQualifiedTypeName();

    /**
     * set element type
     * @param type of element
     */
    void setQualifiedTypeName(String type);

}
