package cc.eamon.open.mapping.mapper;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Eamon on 2018/10/3.
 */
public class MapperIgnoreDetail {

    private String fieldName;

    private Set<String> target = new HashSet<>();

    public void addValue(String value){
        target.add(value);
    }

    public boolean checkIn(String value){
        if(target.contains(value)){
            return true;
        }
        return false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Set<String> getTarget() {
        return target;
    }

    public void setTarget(Set<String> target) {
        this.target = target;
    }
}
