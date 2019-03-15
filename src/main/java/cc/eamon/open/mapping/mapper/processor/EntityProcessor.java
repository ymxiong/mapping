package cc.eamon.open.mapping.mapper.processor;

import cc.eamon.open.mapping.mapper.MapperIgnoreDetail;
import cc.eamon.open.mapping.mapper.MapperModifyDetail;
import cc.eamon.open.mapping.mapper.MapperRenameDetail;
import com.squareup.javapoet.*;
import com.sun.tools.javac.code.Type;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2018-12-25 17:08:50
 */
public class EntityProcessor {

    /**
     * 生成GetEntity方法
     *
     * @param self            原始类包信息
     * @param mapperName      方法名
     * @param elemFields      添加域信息
     * @param ignoreDetailMap ignore信息
     * @param modifyDetailMap modify信息
     * @param renameDetailMap rename信息
     * @param typeSpec        类建造器
     * @return
     */
    public static MethodSpec buildGetEntityMethod(
            ClassName self,
            String mapperName,
            List<Element> elemFields,
            Map<String, MapperIgnoreDetail> ignoreDetailMap,
            Map<String, MapperModifyDetail> modifyDetailMap,
            Map<String, MapperRenameDetail> renameDetailMap,
            TypeSpec.Builder typeSpec
    ) {
        String realMethodName = "getEntity";
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder(realMethodName)
                .addModifiers(Modifier.PUBLIC)
                .returns(self);
        methodSpec.addStatement("$T entity = new $T()", self, self);
        // 添加属性
        for (Element fieldElem : elemFields) {
            String field = fieldElem.getSimpleName().toString();
            if (ignoreDetailMap.get(field) != null) {
                // 如果加了ignore注解针对某方法 则直接跳过
                if (ignoreDetailMap.get(field).checkIn(mapperName)) continue;
            }
            // 有modify信息无rename信息
            if ((modifyDetailMap.get(field) != null) && (renameDetailMap.get(field) == null)) {
                // 检查该属性的modify信息是否与map绑定
                MapperModifyDetail.ModifyDetail modifyDetail = modifyDetailMap.get(field).getValue(mapperName);
                if (modifyDetail != null) {
                    String originName = fieldElem.getSimpleName().toString();
                    String recoverMethodName = modifyDetail.getRecoverName();
                    // recoverMethodName 为空的情况
                    if (recoverMethodName.equals("")) {
                        recoverMethodName = "set" + originName.substring(0, 1).toUpperCase() + originName.substring(1);

                        Type.MethodType recoverType = (Type.MethodType) modifyDetail.getRecoverType();

                        FieldSpec fieldSpec = FieldSpec.builder(
                                TypeName.get(recoverType.getReturnType()),
                                originName,
                                Modifier.PUBLIC)
                                .build();
                        typeSpec.addField(fieldSpec);
                        methodSpec.addStatement("entity." + recoverMethodName + "(this." + originName + ")");
                    }else{
                        Type.MethodType recoverType = (Type.MethodType) modifyDetail.getRecoverType();

                        FieldSpec fieldSpec = FieldSpec.builder(
                                TypeName.get(recoverType.getReturnType()),
                                originName,
                                Modifier.PUBLIC)
                                .build();
                        typeSpec.addField(fieldSpec);
                        methodSpec.addStatement("entity." +
                                        "set" + originName.substring(0, 1).toUpperCase() + originName.substring(1) + "(" +
                                        "entity." + recoverMethodName + "(this." + originName + "))");
                    }
                    continue;
                }
            }
            // 有rename信息无modify信息
            else if ((renameDetailMap.get(field) != null) && (modifyDetailMap.get(field) == null)) {
                // 检查该属性的rename信息是否与map绑定
                if (renameDetailMap.get(field).getValue(mapperName) != null) {
                    String originName = fieldElem.getSimpleName().toString();
                    String fieldName = renameDetailMap.get(field).getValue(mapperName).getRenameName();
                    FieldSpec fieldSpec = FieldSpec.builder(
                            TypeName.get(fieldElem.asType()),
                            fieldName,
                            Modifier.PUBLIC)
                            .build();
                    typeSpec.addField(fieldSpec);

                    methodSpec.addStatement("entity.set" + originName.substring(0, 1).toUpperCase() + originName.substring(1) + "(this." + fieldName + ")");
                    continue;
                }
            }
            // 有rename信息和modify信息
            else if ((modifyDetailMap.get(field) != null) && (renameDetailMap.get(field) != null)) {
                // 检查该属性的modify和rename信息是否都与map绑定
                MapperModifyDetail.ModifyDetail modifyDetail = modifyDetailMap.get(field).getValue(mapperName);
                if ((modifyDetail != null) && (renameDetailMap.get(field).getValue(mapperName) != null)) {
                    String originName = fieldElem.getSimpleName().toString();
                    String fieldName = renameDetailMap.get(field).getValue(mapperName).getRenameName();
                    String recoverMethodName = modifyDetail.getRecoverName();
                    // recoverMethodName 为空的情况
                    if (recoverMethodName.equals("")) {
                        recoverMethodName = "set" + originName.substring(0, 1).toUpperCase() + originName.substring(1);

                        Type.MethodType recoverType = (Type.MethodType) modifyDetail.getRecoverType();

                        FieldSpec fieldSpec = FieldSpec.builder(
                                TypeName.get(recoverType.getReturnType()),
                                fieldName,
                                Modifier.PUBLIC)
                                .build();
                        typeSpec.addField(fieldSpec);
                        methodSpec.addStatement("entity." + recoverMethodName + "(this." + fieldName + ")");
                    }else{
                        Type.MethodType recoverType = (Type.MethodType) modifyDetail.getRecoverType();

                        FieldSpec fieldSpec = FieldSpec.builder(
                                TypeName.get(recoverType.getReturnType()),
                                fieldName,
                                Modifier.PUBLIC)
                                .build();
                        typeSpec.addField(fieldSpec);
                        methodSpec.addStatement("entity." +
                                "set" + originName.substring(0, 1).toUpperCase() + originName.substring(1) + "(" +
                                "entity." + recoverMethodName + "(this." + fieldName + "))");
                    }
                    continue;
                    // 检查该属性的rename信息是否都与map绑定
                } else if (renameDetailMap.get(field).getValue(mapperName) != null) {
                    String originName = fieldElem.getSimpleName().toString();
                    String fieldName = renameDetailMap.get(field).getValue(mapperName).getRenameName();
                    FieldSpec fieldSpec = FieldSpec.builder(
                            TypeName.get(fieldElem.asType()),
                            fieldName,
                            Modifier.PUBLIC)
                            .build();
                    typeSpec.addField(fieldSpec);
                    methodSpec.addStatement("entity.set" + originName.substring(0, 1).toUpperCase() + originName.substring(1) + "(this." + fieldName + ")");
                    continue;
                    // 检查该属性的modify信息是否都与map绑定
                } else if (modifyDetail != null) {
                    String originName = fieldElem.getSimpleName().toString();
                    String recoverMethodName = modifyDetail.getRecoverName();
                    Type.MethodType recoverType = (Type.MethodType) modifyDetail.getRecoverType();

                    FieldSpec fieldSpec = FieldSpec.builder(
                            TypeName.get(recoverType.getReturnType()),
                            originName,
                            Modifier.PUBLIC)
                            .build();
                    typeSpec.addField(fieldSpec);
                    methodSpec.addStatement("entity." + recoverMethodName + "(this." + originName + ")");
                    continue;
                }
            }

            String originName = fieldElem.getSimpleName().toString();
            FieldSpec fieldSpec = FieldSpec.builder(
                    TypeName.get(fieldElem.asType()),
                    originName,
                    Modifier.PUBLIC)
                    .build();
            typeSpec.addField(fieldSpec);
            // 若无属性绑定，直接生成方法信息
            methodSpec.addStatement("entity.set" + originName.substring(0, 1).toUpperCase() + originName.substring(1) + "(this." + originName + ")");


        }


        // 添加返回结果
        methodSpec.addStatement("return entity");
        return methodSpec.build();
    }


}
