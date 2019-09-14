package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 18:08:28
 */
public class ExtendsDetail implements MapperDetail {

    private String packageName;

    private String superMapperName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getSuperMapperName() {
        return superMapperName;
    }

    public void setSuperMapperName(String superMapperName) {
        this.superMapperName = superMapperName;
    }
}
