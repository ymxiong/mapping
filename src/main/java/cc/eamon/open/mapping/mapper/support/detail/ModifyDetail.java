package cc.eamon.open.mapping.mapper.support.detail;

import cc.eamon.open.mapping.mapper.structure.detail.MapperDetail;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:12:22
 */
public class ModifyDetail implements MapperDetail {

    private String modifyQualifiedTypeName;

    private String modifyMethodName;

    private String recoverMethodName;

    public String getModifyQualifiedTypeName() {
        return modifyQualifiedTypeName;
    }

    public void setModifyQualifiedTypeName(String modifyQualifiedTypeName) {
        this.modifyQualifiedTypeName = modifyQualifiedTypeName;
    }

    public String getModifyMethodName() {
        return modifyMethodName;
    }

    public void setModifyMethodName(String modifyMethodName) {
        this.modifyMethodName = modifyMethodName;
    }

    public String getRecoverMethodName() {
        return recoverMethodName;
    }

    public void setRecoverMethodName(String recoverMethodName) {
        this.recoverMethodName = recoverMethodName;
    }
}
