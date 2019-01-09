package cc.eamon.open.mapping.group;


import cc.eamon.open.mapping.ProcessingException;
import cc.eamon.open.mapping.mapper.Mapper;
import com.squareup.javapoet.*;
import com.sun.tools.javac.code.Type;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2018-12-05 02:59:23
 */

public class GroupProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    public GroupProcessor() {
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(Group.class.getCanonicalName());
        return annotations;
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {

            Map<String, List<GroupDetail>> groupDetailMap = new HashMap<>();
            // 扫描类 过滤掉非类上注解
            for (Element elem : roundEnv.getElementsAnnotatedWith(Group.class)) {
                if (elem.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(elem, "Only classes can be annotated with @%s", Mapper.class.getSimpleName());
                }
                Group group = elem.getAnnotation(Group.class);
                if (group.value().length == 0) continue;
                for (int i = 0; i < group.value().length; i++) {
                    List<GroupDetail> sameGroupList = groupDetailMap.computeIfAbsent(group.value()[i], value -> new ArrayList<>());
                    GroupDetail groupDetail = new GroupDetail();
                    boolean base = group.base().length > i ? group.base()[i] :
                            (group.base().length != 0 && group.base()[group.base().length]);
                    groupDetail.setBase(base);
                    boolean list = group.list().length > i ? group.list()[i] :
                            (group.list().length != 0 && group.list()[group.list().length]);
                    groupDetail.setList(list);
                    String name = group.name().length > i ? group.name()[i] :
                            (group.name().length != 0 ? "":group.name()[group.name().length]);
                    groupDetail.setName(name);
                    String value = group.value().length > i ? group.value()[i] :
                            (group.value().length != 0 ? "":group.value()[group.value().length]);
                    groupDetail.setValue(value);
                    groupDetail.setTypeName(TypeName.get(elem.asType()));
                    for (Element enclosedElement : elem.getEnclosedElements()) {
                        boolean isMapper = enclosedElement.getSimpleName().toString().equals("getMap");
                        if (isMapper && (enclosedElement.getKind() == ElementKind.METHOD)) {
                            groupDetail.setMapper(true);
                            groupDetail.setMapperParameterTypes(((Type.MethodType) enclosedElement.asType()).getParameterTypes());
                            break;
                        }
                    }
                    sameGroupList.add(groupDetail);
                }
            }
            for (Map.Entry<String, List<GroupDetail>> entry : groupDetailMap.entrySet()) {
                if (entry.getValue().size() == 0) continue;
                String groupValue = entry.getValue().get(0).getValue();
                String className = groupValue.replaceAll(".*\\.", "");
                String packageName = groupValue.replaceAll("\\.[^\\.]+$", "");
                // 新建类
                TypeSpec.Builder typeSpec = TypeSpec.classBuilder(className);
                typeSpec.addModifiers(Modifier.PUBLIC);

                // 新建getMap方法
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
//                        .addParameter(self, "obj")
                        .returns(typeOfMap);
                methodSpec.addStatement("Map<String, Object> resultMap = new $T<>()", linkedHashMap);

                // 新增json解析map域
                List<GroupDetail> groupDetails = entry.getValue();
                Collections.sort(groupDetails);
                for (int i = 0; i < groupDetails.size(); i++) {
                    GroupDetail groupDetail = groupDetails.get(i);
                    if (groupDetail.isBase()) {
                        typeSpec.superclass(groupDetail.getTypeName());
                        if (groupDetail.isMapper()) {
                            String objName = "arg" + i;
                            methodSpec.addParameter(TypeName.get(groupDetail.getMapperParameterTypes().get(0)), objName);

                            String statement = "resultMap.putAll(" +
                                    groupDetail.getTypeName().toString().replaceAll(".*\\.", "") +
                                    ".getMap(" +
                                    objName +
                                    "))";
                            methodSpec.addStatement(statement);
                        }
                    } else {
                        TypeName typeName = groupDetail.getTypeName();
                        if (groupDetail.isList()) {
                            ClassName list = ClassName.get("java.util", "List");
                            typeName = ParameterizedTypeName.get(list, typeName);

                            if (groupDetail.isMapper()) {
                                ClassName arrayList = ClassName.get("java.util", "ArrayList");
                                TypeName typeOfParam = ParameterizedTypeName.get(list, TypeName.get(groupDetail.getMapperParameterTypes().get(0)));
                                String objName = "arg" + i;
                                methodSpec.addParameter(typeOfParam, objName);
                                methodSpec.addStatement("List<Map<String, Object>> " + groupDetail.getName() + " = new $T<>()", arrayList);
                                methodSpec.addStatement(objName + ".forEach((e)->" + groupDetail.getName() + ".add($T.getMap(e)))", groupDetail.getTypeName());
                                methodSpec.addStatement("resultMap.put(\"" + groupDetail.getName() + "\", " + groupDetail.getName() + ")");
                            }
                        } else {
                            if (groupDetail.isMapper()) {
                                String objName = "arg" + i;
                                methodSpec.addParameter(TypeName.get(groupDetail.getMapperParameterTypes().get(0)), objName);
                                methodSpec.addStatement("resultMap.put(\"" + groupDetail.getName() + "\", $T.getMap(" + objName + "))", groupDetail.getTypeName());
                            }
                        }
                        FieldSpec fieldSpec = FieldSpec.builder(
                                typeName,
                                groupDetail.getName(),
                                Modifier.PUBLIC)
                                .build();
                        typeSpec.addField(fieldSpec);
                    }
                }

                methodSpec.addStatement("return resultMap");
                typeSpec.addMethod(methodSpec.build());
                // 写出数据
                JavaFile.builder(packageName, typeSpec.build()).build().writeTo(filer);
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
