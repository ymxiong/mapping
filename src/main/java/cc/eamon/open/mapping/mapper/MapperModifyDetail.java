package cc.eamon.open.mapping.mapper;


import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eamon on 2018/10/3.
 */
public class MapperModifyDetail {

    private String fieldName;

    private Map<String, ModifyDetail> target = new HashMap<>();

    public void addValue(ModifyDetail value){
        if (value == null) return;
        target.put(value.getTargetMapName(), value);
    }

    public ModifyDetail getValue(String value){
        if(target.get(value)!=null){
            return target.get(value);
        }
        return null;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Map<String, ModifyDetail> getTarget() {
        return target;
    }

    public void setTarget(Map<String, ModifyDetail> target) {
        this.target = target;
    }

    public static class ModifyDetail {

        private String targetMapName;

        private String modifyName;

        private String recoverName;

        private TypeMirror recoverType;

        public String getTargetMapName() {
            return targetMapName;
        }

        public void setTargetMapName(String targetMapName) {
            this.targetMapName = targetMapName;
        }

        public String getModifyName() { return modifyName; }

        public void setModifyName(String modifyName) { this.modifyName = modifyName; }

        public String getRecoverName() { return recoverName; }

        public void setRecoverName(String recoverName) { this.recoverName = recoverName;}

        public TypeMirror getRecoverType() { return recoverType; }

        public void setRecoverType(TypeMirror recoverType) { this.recoverType = recoverType; }
    }

}
