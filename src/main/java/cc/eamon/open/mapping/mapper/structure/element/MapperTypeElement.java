package cc.eamon.open.mapping.mapper.structure.element;

import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.context.MapperContextHolder;
import cc.eamon.open.mapping.mapper.structure.factory.MapperBaseFactory;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperMethod;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import com.google.common.collect.Lists;

import javax.lang.model.element.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-23 19:09:45
 */
public class MapperTypeElement {

    private PackageElement packageElement;

    private TypeElement typeElement;

    public MapperTypeElement(PackageElement packageElement, TypeElement typeElement) throws Exception {
        this.packageElement = packageElement;
        this.typeElement = typeElement;

        // build mapper
        Mapper mapper = typeElement.getAnnotation(Mapper.class);
        if (mapper == null) {
            // TODO: ALARM INFO EXCEPTION THROW
            throw new Exception();
        }

        // store default
        MapperContextHolder.get().getMappers().add("default");
        // store type value info
        MapperContextHolder.get().getMappers().addAll(Lists.newArrayList(mapper.value()));

        // store method info
        for (Element elem : typeElement.getEnclosedElements()) {
            if (elem.getKind() == ElementKind.METHOD) {
                MapperContextHolder.get().getMethodMap().put(elem.getSimpleName().toString(), (ExecutableElement) elem);
            }
        }

        // store field info
        for (Element elem : typeElement.getEnclosedElements()) {
            if (elem.getKind().isField()) {
                MapperContextHolder.get().getFieldMap().put(elem.getSimpleName().toString(), (VariableElement) elem);
            }
        }
    }

    // 关联变量、方法、生成对应的mapper
    // 扫描父类
    // 如果父类有Mapper注解
    // 继承父类同名
    // 扫描父类域下其他public信息
    // 扫描域下其他信息
    public Set<MapperType> build() {

        Set<MapperType> mapperTypeSet = new HashSet<>();

        // build mapper
        MapperContextHolder.get().getMappers().forEach((mapper) -> {

            // build field
            List<MapperField> fields = new ArrayList<>();
            MapperContextHolder.get().getFieldMap().forEach((field, element) -> {

                // field define
                MapperField mapperField = new MapperField();

                // set basic info
                mapperField.setMapperName(mapper);
                mapperField.setSimpleName(element.getSimpleName().toString());
                mapperField.setQualifiedTypeName(element.asType().toString());


                // set annotation info
                mapperField.setStrategies(MapperBaseFactory.buildStrategies(element, mapper));
                fields.add(mapperField);
            });

            // build method
            List<MapperMethod> methods = new ArrayList<>();
            MapperContextHolder.get().getMethodMap().forEach((method, element) -> {
                MapperMethod mapperMethod = new MapperMethod();
                mapperMethod.setMapperName(mapper);
                mapperMethod.setSimpleName(method);
                methods.add(mapperMethod);
            });

            // build type
            MapperType mapperType = MapperType.builder()
                    .mapperName(mapper)
                    .packageName(packageElement.isUnnamed() ? "" : packageElement.getQualifiedName().toString())
                    .simpleName(typeElement.getSimpleName().toString())
                    .qualifiedName(typeElement.getQualifiedName().toString())
                    .mapperFieldList(fields)
                    .mapperMethodList(methods)
                    .mapperStrategies(MapperBaseFactory.buildStrategies(typeElement, mapper))
                    .build();
            mapperTypeSet.add(mapperType);
        });

        return mapperTypeSet;
    }

    public PackageElement getPackageElement() {
        return packageElement;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

}
