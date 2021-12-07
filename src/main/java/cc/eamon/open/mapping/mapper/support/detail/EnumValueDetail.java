package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;



/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2021-11-18 10:25:05
 */
public class EnumValueDetail implements MapperDetail {

    String enumClass;

    String enumMethod;

    public String getEnumClass() {
        return enumClass;
    }

    public void setEnumClass(String enumClass) {
        this.enumClass = enumClass;
    }

    public String getEnumMethod() {
        return enumMethod;
    }

    public void setEnumMethod(String enumMethod) {
        this.enumMethod = enumMethod;
    }
}
