package cc.eamon.open.mapping.mapper.util;

import com.sun.tools.javac.code.Type;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-11-02 01:59:22
 */
public class MapperUtils {

    /**
     * build annotation values from AnnotationMirror
     *
     * @param annotationMirror javax.lang.model.element.AnnotationMirror
     * @return values map for annotation fields
     */
    public static Map<String, Object> buildAnnotationFieldsMap(AnnotationMirror annotationMirror) {
        Map<String, Object> annotationValuesMap = new HashMap<>();
        if (annotationMirror == null) {
            return annotationValuesMap;
        }
        // put <annotation field, annotation value from annotationMirror>
        annotationMirror.getElementValues().forEach(
                (key, value) -> annotationValuesMap.put(key.getSimpleName().toString(), value.getValue())
        );
        return annotationValuesMap;
    }

    /**
     * build string annotation value list
     * usage for build string annotation value list
     *
     * @param annotationValuesMap values map for annotation value
     * @param key                 filter key
     * @return string values
     */
    public static List<TypeMirror> buildAnnotationValuesToTypeMirrorList(Map<String, Object> annotationValuesMap, String key) {
        List<TypeMirror> typeMirrorList = new ArrayList<>();
        if (annotationValuesMap.get(key) == null) {
            return typeMirrorList;
        }

        // build target field key to string list
        if (annotationValuesMap.get(key) instanceof List) {
            List annotationValueList = (List) annotationValuesMap.get(key);
            for (Object annotationValueObject : annotationValueList) {
                if (((AnnotationValue) annotationValueObject).getValue() instanceof TypeMirror) {
                    typeMirrorList.add((TypeMirror) ((AnnotationValue) annotationValueObject).getValue());
                }
            }
        }
        return typeMirrorList;
    }

    /**
     * get a single annotation mirror by target type name from a AnnotationMirror list
     *
     * @param annotationMirrors annotation mirror list
     * @param typeName          type name
     * @return single annotation mirror
     */
    public static AnnotationMirror buildAnnotationMirrorByTypeName(List<? extends AnnotationMirror> annotationMirrors, String typeName) {
        AnnotationMirror annotationMirror = null;
        for (Object mirror : annotationMirrors) {
            if (mirror instanceof AnnotationMirror) {
                AnnotationMirror compound = (AnnotationMirror) mirror;
                if (compound.getAnnotationType().toString().equals(typeName)) {
                    annotationMirror = compound;
                    break;
                }
            }
        }
        return annotationMirror;
    }

    /**
     * load super class element from type element
     *
     * @param typeElement typeElement
     * @return element
     */
    public static TypeElement loadSuperTypeElement(TypeElement typeElement) {
        return (TypeElement) ((Type.ClassType) typeElement.getSuperclass()).tsym;
    }

    /**
     * load type arguments of a typeMirror
     *
     * @param typeMirror typeMirror
     * @return list of type mirror
     */
    public static List<TypeMirror> loadTypeArguments(TypeMirror typeMirror) {
        return new ArrayList<>(((Type.ClassType) typeMirror).getTypeArguments());
    }

    /**
     * load method return type name of a method
     *
     * @param executableElement executableElement
     * @return method return object type name
     */
    public static TypeMirror loadMethodReturnTypeName(ExecutableElement executableElement) {
        Type.MethodType methodType = (Type.MethodType) executableElement.asType();
        return methodType.getReturnType();
    }

}
