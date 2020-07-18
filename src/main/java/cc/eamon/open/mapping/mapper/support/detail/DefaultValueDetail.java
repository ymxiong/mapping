package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2020-04-01 22:25:05
 */
public class DefaultValueDetail implements MapperDetail {
    private String defaultValue;

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
