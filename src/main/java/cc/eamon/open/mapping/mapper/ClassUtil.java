package cc.eamon.open.mapping.mapper;

import com.squareup.javapoet.ClassName;


/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-15 03:02:15
 */
public class ClassUtil {

    public static ClassName get(String qualifiedName){
        return ClassName.get(StringUtil.packageNameFromQualifiedName(qualifiedName), StringUtil.classNameFromQualifiedName(qualifiedName));
    }

}
