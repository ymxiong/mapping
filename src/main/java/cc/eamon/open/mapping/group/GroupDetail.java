package cc.eamon.open.mapping.group;


import com.squareup.javapoet.TypeName;
import com.sun.tools.javac.code.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eamon on 2018/10/3.
 */
public class GroupDetail implements Comparable<GroupDetail> {

    private boolean base = false;

    private boolean list = false;

    private boolean isMapper = false;

    private List<Type> mapperParameterTypes = new ArrayList<>();

    private String value = "";

    private String name = "";

    private TypeName typeName = null;

    public boolean isBase() {
        return base;
    }

    public void setBase(boolean base) {
        this.base = base;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isMapper() {
        return isMapper;
    }

    public void setMapper(boolean mapper) {
        isMapper = mapper;
    }

    public List<Type> getMapperParameterTypes() {
        return mapperParameterTypes;
    }

    public void setMapperParameterTypes(List<Type> mapperParameterTypes) {
        this.mapperParameterTypes = mapperParameterTypes;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeName getTypeName() {
        return typeName;
    }

    public void setTypeName(TypeName typeName) {
        this.typeName = typeName;
    }

    @Override
    public int compareTo(GroupDetail o) {
        if (this.isBase()) return -1;
        if (o.isBase()) return 1;
        if (this.getName().length() != o.getName().length()) return this.getName().length() - o.getName().length();
        int diff = 0;
        for (int i = 0; i < this.getName().length(); i++) {
            diff = compareChar(this.getName().charAt(i), o.getName().charAt(i));
            if (diff != 0) break;
        }
        return diff;
    }

    private int compareChar(char a, char b) {
        return a - b;
    }
}
