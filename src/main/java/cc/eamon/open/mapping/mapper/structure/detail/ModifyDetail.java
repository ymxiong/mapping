package cc.eamon.open.mapping.mapper.structure.detail;

import javax.lang.model.type.TypeMirror;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 14:12:22
 */
public class ModifyDetail implements MapperDetail {

    private TypeMirror modifyType;

    private String modifyMethodName;

    private String recoverMethodName;

    public TypeMirror getModifyType() {
        return modifyType;
    }

    public void setModifyType(TypeMirror modifyType) {
        this.modifyType = modifyType;
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
