package cc.eamon.open.mapping.mapper.processor;

import cc.eamon.open.mapping.ProcessingException;
import cc.eamon.open.mapping.mapper.MapperExtra;
import cc.eamon.open.mapping.mapper.MapperIgnoreDetail;
import cc.eamon.open.mapping.mapper.MapperModifyDetail;
import cc.eamon.open.mapping.mapper.MapperRenameDetail;
import com.squareup.javapoet.*;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2018-12-25 17:11:23
 */
public class MapProcessor {


    public static MethodSpec buildGetMapExtraMethod(
            ClassName self,
            String mapperName,
            List<Element> elemFields,
            Map<String, MapperIgnoreDetail> ignoreDetailMap,
            Map<String, MapperModifyDetail> modifyDetailMap,
            Map<String, MapperRenameDetail> renameDetailMap,
            MapperExtra mapperExtra,
            TypeElement typeElement,
            TypeSpec.Builder typeSpec
    ) throws ProcessingException {


        // 构造方法
        // 确定需要import的项
        ClassName string = ClassName.get("java.lang", "String");
        ClassName object = ClassName.get("java.lang", "Object");
        ClassName map = ClassName.get("java.util", "Map");
        ClassName linkedHashMap = ClassName.get("java.util", "LinkedHashMap");
        TypeName typeOfMap = ParameterizedTypeName.get(map, string, object);

        String realMethodName = "getMapWithExtra";
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(realMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(typeOfMap);

        // 创建resultMap
        methodSpec.addStatement("Map<String, Object> resultMap = new $T<>()", linkedHashMap);
        methodSpec.addStatement("if (obj == null) return resultMap");
        mapProc(mapperName, elemFields, ignoreDetailMap, modifyDetailMap, renameDetailMap, methodSpec);


        int index = 0;
        for (int i = 0; i < mapperExtra.value().length; i++) {
            if (mapperName.equals(mapperExtra.value()[i])) {
                index = i;

                String clazz;
                String name;
                boolean list;

                if (mapperExtra.type().length > 0 && mapperExtra.type().length <= index) {
                    clazz = mapperExtra.type()[mapperExtra.list().length - 1];
                } else if (mapperExtra.type().length > 0) {
                    clazz = mapperExtra.type()[index];
                } else {
                    throw new ProcessingException(typeElement, "MapperExtra: Type Can't Be Empty");
                }

                if (mapperExtra.name().length > 0 && mapperExtra.name().length <= index) {
                    name = mapperExtra.name()[mapperExtra.list().length - 1];
                } else if (mapperExtra.name().length > 0) {
                    name = mapperExtra.name()[index];
                } else {
                    throw new ProcessingException(typeElement, "MapperExtra: Name Can't Be Empty");
                }

                if (mapperExtra.list().length > 0 && mapperExtra.list().length <= index) {
                    list = mapperExtra.list()[mapperExtra.list().length - 1];
                } else if (mapperExtra.list().length > 0) {
                    list = mapperExtra.list()[index];
                } else {
                    list = false;
                }
                String pkgName = clazz.replaceAll("\\.[^\\.]+$", "");
                String clazzName = clazz.replaceAll(".*\\.", "");
                TypeName className = ClassName.get(pkgName, clazzName);
                if (clazzName.equals("Map")) className = typeOfMap;
                // 生产一个list
                if (list) {
                    ClassName listClazz = ClassName.get("java.util", "List");
                    FieldSpec fieldSpec = FieldSpec.builder(
                            ParameterizedTypeName.get(listClazz, className),
                            name,
                            Modifier.PUBLIC)
                            .build();
                    typeSpec.addField(fieldSpec);
                    methodSpec.addParameter(ParameterizedTypeName.get(listClazz, className), name);
                    methodSpec.addStatement("resultMap.put(\"" + name + "\", " + name + ")");
                } else {
                    FieldSpec fieldSpec = FieldSpec.builder(
                            className,
                            name,
                            Modifier.PUBLIC)
                            .build();
                    typeSpec.addField(fieldSpec);
                    methodSpec.addParameter(className, name);
                    methodSpec.addStatement("resultMap.put(\"" + name + "\", " + name + ")");
                }
            }
        }

        // 添加返回结果
        methodSpec.addStatement("return resultMap");
        return methodSpec.build();
    }

    /**
     * 生成GetMap方法
     *
     * @param self            原始类包信息
     * @param mapperName      方法名
     * @param elemFields      添加域信息
     * @param ignoreDetailMap ignore信息
     * @param modifyDetailMap modify信息
     * @param renameDetailMap rename信息
     * @return
     */
    public static MethodSpec buildGetMapMethod(
            ClassName self,
            String mapperName,
            List<Element> elemFields,
            Map<String, MapperIgnoreDetail> ignoreDetailMap,
            Map<String, MapperModifyDetail> modifyDetailMap,
            Map<String, MapperRenameDetail> renameDetailMap
    ) {
        // 确定需要import的项
        ClassName string = ClassName.get("java.lang", "String");
        ClassName object = ClassName.get("java.lang", "Object");
        ClassName map = ClassName.get("java.util", "Map");
        ClassName linkedHashMap = ClassName.get("java.util", "LinkedHashMap");
        TypeName typeOfMap = ParameterizedTypeName.get(map, string, object);

        String realMethodName = "getMap";
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(realMethodName)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(typeOfMap);

        // 创建resultMap
        methodSpec.addStatement("Map<String, Object> resultMap = new $T<>()", linkedHashMap);
        methodSpec.addStatement("if (obj == null) return resultMap");
        mapProc(mapperName, elemFields, ignoreDetailMap, modifyDetailMap, renameDetailMap, methodSpec);
        // 添加返回结果
        methodSpec.addStatement("return resultMap");
        return methodSpec.build();
    }

    private static void mapProc(String mapperName, List<Element> elemFields, Map<String, MapperIgnoreDetail> ignoreDetailMap, Map<String, MapperModifyDetail> modifyDetailMap, Map<String, MapperRenameDetail> renameDetailMap, MethodSpec.Builder methodSpec) {
        for (Element fieldElem : elemFields) {
            String field = fieldElem.getSimpleName().toString();
            if (ignoreDetailMap.get(field) != null) {
                // 如果加了ignore注解针对某方法 则直接跳过
                if (ignoreDetailMap.get(field).checkIn(mapperName)) continue;
            }
            // 有modify信息无rename信息
            if ((modifyDetailMap.get(field) != null) && (renameDetailMap.get(field) == null)) {
                // 检查该属性的modify信息是否与map绑定
                if (modifyDetailMap.get(field).getValue(mapperName) != null) {
                    MapperModifyDetail.ModifyDetail detail = modifyDetailMap.get(field).getValue(mapperName);
//                                methodSpec.addStatement("resultMap.put(\"" + field + "\", " + simpleName.toString() + "." + detail.getMethodName() + "(obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "()))");
                    methodSpec.addStatement("resultMap.put(\"" + field + "\", " + "obj." + detail.getModifyName() + "(obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "()))");
                    continue;
                }
            }
            // 有rename信息无modify信息
            else if ((renameDetailMap.get(field) != null) && (modifyDetailMap.get(field) == null)) {
                // 检查该属性的rename信息是否与map绑定
                if (renameDetailMap.get(field).getValue(mapperName) != null) {
                    MapperRenameDetail.RenameDetail detail = renameDetailMap.get(field).getValue(mapperName);
                    methodSpec.addStatement("resultMap.put(\"" + detail.getRenameName() + "\", " + "obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "())");
                    continue;
                }
            }
            // 有rename信息和modify信息
            else if ((modifyDetailMap.get(field) != null) && (renameDetailMap.get(field) != null)) {
                // 检查该属性的modify和rename信息是否都与map绑定
                if ((modifyDetailMap.get(field).getValue(mapperName) != null) && (renameDetailMap.get(field).getValue(mapperName) != null)) {
                    MapperModifyDetail.ModifyDetail modifyDetail = modifyDetailMap.get(field).getValue(mapperName);
                    MapperRenameDetail.RenameDetail renameDetail = renameDetailMap.get(field).getValue(mapperName);
                    methodSpec.addStatement("resultMap.put(\"" + renameDetail.getRenameName() + "\", " + "obj." + modifyDetail.getModifyName() + "(obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "()))");
                    continue;
                    // 检查该属性的rename信息是否都与map绑定
                } else if (renameDetailMap.get(field).getValue(mapperName) != null) {
                    MapperRenameDetail.RenameDetail detail = renameDetailMap.get(field).getValue(mapperName);
                    methodSpec.addStatement("resultMap.put(\"" + detail.getRenameName() + "\", " + "obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "())");
                    continue;
                    // 检查该属性的modify信息是否都与map绑定
                } else if (modifyDetailMap.get(field).getValue(mapperName) != null) {
                    MapperModifyDetail.ModifyDetail detail = modifyDetailMap.get(field).getValue(mapperName);
                    methodSpec.addStatement("resultMap.put(\"" + field + "\", " + "obj." + detail.getModifyName() + "(obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "()))");
                    continue;
                }
            }
            // 若无属性绑定，直接生成方法信息
            methodSpec.addStatement("resultMap.put(\"" + field + "\", obj.get" + field.substring(0, 1).toUpperCase() + field.substring(1) + "())");

        }
    }


}
