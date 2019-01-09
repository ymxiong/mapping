package cc.eamon.open.mapping.mapper;

import cc.eamon.open.mapping.ProcessingException;
import cc.eamon.open.mapping.mapper.processor.EntityProcessor;
import cc.eamon.open.mapping.mapper.processor.MapGroupProcessor;
import cc.eamon.open.mapping.mapper.processor.MapProcessor;
import com.squareup.javapoet.*;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementScanner6;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.*;

/**
 * Created by Eamon on 2018/9/30.
 */
public class MapperProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    public MapperProcessor() {
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
        annotations.add(Mapper.class.getCanonicalName());
        return annotations;
    }


    private class NameCheckScanner extends ElementScanner6<Void, Void> {

        @Override
        public Void scan(Element e, Void aVoid) {
            return super.scan(e, aVoid);
        }

    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 扫描类上注解
     *
     * @param elem            类信息
     * @param elemFields      属性信息
     * @param ignoreDetailMap ignore信息
     * @param modifyDetailMap modify信息
     * @param renameDetailMap rename信息
     */
    private void scanFields(
            Element elem,
            List<Element> elemFields,
            Map<String, MapperIgnoreDetail> ignoreDetailMap,
            Map<String, MapperModifyDetail> modifyDetailMap,
            Map<String, MapperRenameDetail> renameDetailMap) {
        Map<String, TypeMirror> methodType = new HashMap<>();

        for (Element elemMethod : elem.getEnclosedElements()) {
            if (elemMethod.getKind() == ElementKind.METHOD) {
                methodType.put(elemMethod.getSimpleName().toString(), elemMethod.asType());
            }
        }

        // 扫描ignore modify和rename项
        for (Element elemField : elem.getEnclosedElements()) {
            if (elemField.getKind().isField()) {
                // 排除static属性
                if (elemField.getModifiers().contains(Modifier.STATIC)) continue;

                elemFields.add(elemField);
                MapperIgnore ignore = elemField.getAnnotation(MapperIgnore.class);
                MapperModify modify = elemField.getAnnotation(MapperModify.class);
                MapperRename rename = elemField.getAnnotation(MapperRename.class);

                // 记录ignore
                if (ignore != null) {
                    MapperIgnoreDetail detail = new MapperIgnoreDetail();
                    detail.setFieldName(elemField.getSimpleName().toString());
                    for (String s : ignore.value()) {
                        detail.addValue(s);
                    }
                    ignoreDetailMap.put(detail.getFieldName(), detail);
                }
                // 记录modify及方法
                if (modify != null) {
                    MapperModifyDetail detail = new MapperModifyDetail();
                    detail.setFieldName(elemField.getSimpleName().toString());

                    // 生成detail信息
                    for (int i = 0; i < modify.value().length; i++) {
                        MapperModifyDetail.ModifyDetail modifyDetail = new MapperModifyDetail.ModifyDetail();
                        modifyDetail.setModifyName(modify.modify()[i]);
                        modifyDetail.setRecoverName(modify.recover()[i]);
                        modifyDetail.setTargetMapName(modify.value()[i]);
                        modifyDetail.setRecoverType(methodType.get(modify.modify()[i]));
                        detail.addValue(modifyDetail);
                    }
                    modifyDetailMap.put(detail.getFieldName(), detail);
                }
                // 记录rename
                if (rename != null) {
                    MapperRenameDetail detail = new MapperRenameDetail();
                    detail.setFieldName(elemField.getSimpleName().toString());

                    // 生成detail信息
                    for (int i = 0; i < rename.value().length; i++) {
                        MapperRenameDetail.RenameDetail renameDetail = new MapperRenameDetail.RenameDetail();
                        renameDetail.setTargetMapName(rename.value()[i]);
                        renameDetail.setRenameName(rename.name()[i]);
                        detail.addValue(renameDetail);
                    }
                    renameDetailMap.put(detail.getFieldName(), detail);
                }

            }
        }
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // 扫描类 过滤掉非类上注解
            for (Element elem : roundEnv.getElementsAnnotatedWith(Mapper.class)) {
                if (elem.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(elem, "Only classes can be annotated with @%s", Mapper.class.getSimpleName());
                }

                // 定义ignore modify和rename项
                List<Element> fields = new ArrayList<>();
                Map<String, MapperIgnoreDetail> ignoreDetailMap = new HashMap<>();
                Map<String, MapperModifyDetail> modifyDetailMap = new HashMap<>();
                Map<String, MapperRenameDetail> renameDetailMap = new HashMap<>();

                // 扫描ignore modify和rename项
                scanFields(elem, fields, ignoreDetailMap, modifyDetailMap, renameDetailMap);


                // 获取type信息
                TypeElement typeElement = (TypeElement) elem;

                Name qualifiedClassName = typeElement.getQualifiedName();
                Name simpleName = typeElement.getSimpleName();

                // 获取pkg信息
                PackageElement pkg = elementUtils.getPackageOf(typeElement);
                String packageName = pkg.isUnnamed() ? "" : pkg.getQualifiedName().toString();

                // 定义Mapper
                Mapper mapper = elem.getAnnotation(Mapper.class);
                // 检测MapperExtra
                MapperExtra mapperExtra = elem.getAnnotation(MapperExtra.class);
                // 检测MapperGroup
                MapperGroup mapperGroup = elem.getAnnotation(MapperGroup.class);

                // 获取Mapper列表
                HashSet<String> mapperSet = new HashSet<>();
                mapperSet.add("default");
                mapperSet.addAll(Arrays.asList(mapper.value()));

                // 鲁棒化mapperExtra
                HashSet<String> mapperExtraSet = new HashSet<>();
                if (mapperExtra != null) {
                    // 获取GroupMapper列表
                    if (mapperExtra.value().length == 0) {
                        mapperExtraSet.add("default");
                    } else {
                        mapperExtraSet.addAll(Arrays.asList(mapperExtra.value()));
                    }
                }

                // 鲁棒化mapperGroup
                HashSet<String> mapperGroupSet = new HashSet<>();
                if (mapperGroup != null) {
                    // 获取GroupMapper列表
                    if (mapperGroup.target().length == 0) {
                        mapperGroupSet.add("default");
                    } else {
                        mapperGroupSet.addAll(Arrays.asList(mapperGroup.target()));
                    }
                }

                // 确定import本类
                ClassName self = ClassName.get(packageName, simpleName.toString());

                for (String mapperName : mapperSet) {

                    // 新建类
                    TypeSpec.Builder typeSpec = TypeSpec.classBuilder(simpleName.toString() + (mapperName.charAt(0) + "").toUpperCase() + mapperName.substring(1).toLowerCase() + "Mapper");
                    typeSpec.addModifiers(Modifier.PUBLIC);

                    // 出现MapperGroup注解
                    if (mapperGroup != null) {
                        if (mapperGroupSet.contains(mapperName)) {
                            MapGroupProcessor.buildMapGroup(mapperGroup, mapperName, typeSpec);
                        }
                    }

                    // 生成getMap方法
                    MethodSpec getMapMethod = MapProcessor.buildGetMapMethod(self, mapperName, fields, ignoreDetailMap, modifyDetailMap, renameDetailMap);
                    // 添加方法
                    typeSpec.addMethod(getMapMethod);
                    // 出现MapperExtra注解
                    if (mapperExtra != null) {
                        if (mapperExtraSet.contains(mapperName)) {
                            MethodSpec getMapExtraMethod = MapProcessor.buildGetMapExtraMethod(self, mapperName, fields, ignoreDetailMap, modifyDetailMap, renameDetailMap, mapperExtra, typeElement, typeSpec);
                            typeSpec.addMethod(getMapExtraMethod);
                        }
                    }

                    // 生成getEntity方法
                    MethodSpec getEntityMethod = EntityProcessor.buildGetEntityMethod(self, mapperName, fields, ignoreDetailMap, modifyDetailMap, renameDetailMap, typeSpec);
                    // 添加方法
                    typeSpec.addMethod(getEntityMethod);

                    // 写出数据
                    JavaFile.builder(packageName, typeSpec.build()).build().writeTo(filer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
