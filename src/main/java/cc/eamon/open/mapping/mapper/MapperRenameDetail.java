package cc.eamon.open.mapping.mapper;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eamon on 2018/10/3.
 */
public class MapperRenameDetail {

    private String fieldName;

    private Map<String, RenameDetail> target = new HashMap<>();

    public void addValue(RenameDetail value){
        if (value == null) return;
        target.put(value.getTargetMapName(), value);
    }

    public RenameDetail getValue(String value){
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

    public Map<String, RenameDetail> getTarget() {
        return target;
    }

    public void setTarget(Map<String, RenameDetail> target) {
        this.target = target;
    }

    public static class RenameDetail {

        private String targetMapName;

        private String renameName;

        public String getTargetMapName() {
            return targetMapName;
        }

        public void setTargetMapName(String targetMapName) {
            this.targetMapName = targetMapName;
        }

        public String getRenameName() {
            return renameName;
        }

        public void setRenameName(String renameName) {
            this.renameName = renameName;
        }
    }

}
