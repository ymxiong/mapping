package cc.eamon.open.mapping.mapper.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;


/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-15 03:02:15
 */
public class ClassUtils {

    public static ClassName get(String qualifiedName){
        return ClassName.get(StringUtils.packageNameFromQualifiedName(qualifiedName), StringUtils.classNameFromQualifiedName(qualifiedName));
    }

    public static ClassName getString(){
        return ClassName.get("java.lang", "String");
    }

    public static ClassName getObject(){
        return ClassName.get("java.lang", "Object");
    }

    public static ClassName getMap(){
        return ClassName.get("java.util", "Map");
    }

    public static ClassName getList(){
        return ClassName.get("java.util", "List");
    }

    public static ClassName getLinkedHashMap(){
        return ClassName.get("java.util", "LinkedHashMap");
    }

    public static TypeName getParameterizedObjectMap(){
        return ParameterizedTypeName.get(getMap(), getString(), getObject());
    }

    public static TypeName getParameterizedStringMap(){
        return ParameterizedTypeName.get(getMap(), getString(), getString());
    }

    public static TypeName getParameterizedList(TypeName typeName){
        return ParameterizedTypeName.get(getList(), typeName);
    }

}
