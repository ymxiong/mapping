package cc.eamon.open.mapping.mapper.processor;

import cc.eamon.open.mapping.exception.ProcessingException;
import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.context.MapperContextHolder;
import cc.eamon.open.mapping.mapper.structure.element.MapperTypeElement;
import cc.eamon.open.mapping.mapper.support.MapperBuilder;
import com.squareup.javapoet.JavaFile;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // 扫描类 过滤掉非类上注解
            Map<String, Element> mapperElements = new HashMap<>();
            roundEnv.getElementsAnnotatedWith(Mapper.class).forEach((element)-> mapperElements.put(((TypeElement)element).getQualifiedName().toString(), element));

            for (Element elem : roundEnv.getElementsAnnotatedWith(Mapper.class)) {
                if (elem.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(elem, "Only classes can be annotated with @%s", Mapper.class.getSimpleName());
                }

                // 获取type信息
                TypeElement typeElement = (TypeElement) elem;
                // 获取pkg信息
                PackageElement packageElement = elementUtils.getPackageOf(typeElement);

                // 建立mapperElement
                MapperContextHolder.init();
                MapperContextHolder.get().getMapperElements().putAll(mapperElements);
                MapperTypeElement mapperElement = new MapperTypeElement(packageElement, typeElement);
                mapperElement.build().forEach(
                        mapperType -> {
                            try {
                                JavaFile.builder(mapperType.getPackageName(), MapperBuilder.build(mapperType)).build().writeTo(filer);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                );
                MapperContextHolder.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
