package cc.eamon.open.mapping.mapper.processor;

import cc.eamon.open.mapping.ast.factory.DefaultPipelineFactory;
import cc.eamon.open.mapping.ast.factory.PipelineFactory;
import cc.eamon.open.mapping.ast.handler.AST;
import cc.eamon.open.mapping.ast.item.MapperInfo;
import cc.eamon.open.mapping.exception.ProcessingException;
import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.context.MapperContextHolder;
import cc.eamon.open.mapping.mapper.structure.element.MapperTypeElement;
import cc.eamon.open.mapping.mapper.support.MapperBuilder;
import com.squareup.javapoet.JavaFile;
import com.sun.source.tree.Tree;
import com.sun.source.util.TreePath;
import com.sun.tools.javac.api.JavacTrees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;

/**
 * Created by Eamon on 2018/9/30.
 */
public class MapperProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    private JavacTrees javacTrees; // 提供了待处理的抽象语法树
    private TreeMaker treeMaker; // 封装了创建AST节点的一些方法
    private Names names; // 提供了创建标识符的方法

    private static Logger logger = LoggerFactory.getLogger(MapperProcessor.class);

    public MapperProcessor() {
    }


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
        javacTrees = JavacTrees.instance(processingEnv);
        Context context = ((JavacProcessingEnvironment) processingEnv).getContext();
        treeMaker = TreeMaker.instance(context);
        names = Names.instance(context);
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

        // 扫描类 过滤掉非类上注解
        Map<String, Element> mapperElements = new HashMap<>();
        roundEnv.getElementsAnnotatedWith(Mapper.class).forEach((element) -> mapperElements.put(((TypeElement) element).getQualifiedName().toString(), element));

        logger.info("Mapping elements: " + mapperElements.values().toString());
        for (Element elem : roundEnv.getElementsAnnotatedWith(Mapper.class)) {
            try {
                if (elem.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(elem, "Only classes can be annotated with @%s", Mapper.class.getSimpleName());
                }
                JCTree tree = javacTrees.getTree(elem);
                // 获取type信息
                TypeElement typeElement = (TypeElement) elem;
                // 获取pkg信息
                PackageElement packageElement = elementUtils.getPackageOf(typeElement);

                // 建立mapperElement
                logger.info("Mapping init context for " + elem.getSimpleName());
                MapperContextHolder.init();
                MapperContextHolder.get().getMapperElements().putAll(mapperElements);
                MapperTypeElement mapperElement = new MapperTypeElement(packageElement, typeElement);
                mapperElement.build().forEach(
                        mapperType -> {
                            try {
                                JavaFile.builder(mapperType.getPackageName(), MapperBuilder.build(mapperType)).build().writeTo(filer);
                            } catch (IOException e) {
                                logger.error("Mapping type build error:" + elem.getSimpleName() + "-" + mapperType.getQualifiedName() + "-" + e.toString());
                            }
                        }
                );
                tree.accept(new TreeTranslator(){
                    @Override
                    public void visitClassDef(JCTree.JCClassDecl jcClassDecl){
                        importClasses(elem);
                        AST tree = new AST();
                        mapperElement.build().forEach(mapperType -> {
                            tree.getFieldTree().put(mapperType.getMapperName(), new ListBuffer<>());
                            tree.getMethodTree().put(mapperType.getMapperName(), new ListBuffer<>());
                            PipelineFactory factory = new DefaultPipelineFactory();
                            MapperInfo mapperInfo = new MapperInfo(mapperType);
                            factory.buildPipeline(tree, mapperInfo, jcClassDecl, treeMaker, names);
                        });
                        jcClassDecl.defs = jcClassDecl.defs.prependList(tree.build());

                    }
                });
                MapperContextHolder.clear();
                logger.info("Mapping clear context for " + elem.getSimpleName());
            } catch (Exception e) {
                logger.error("Mapping element build error: " + elem.getSimpleName() + "-" + e.toString());
            }
        }
        return false;
    }

    private void importClasses(Element elem){
        TreePath treePath = javacTrees.getPath(elem);
        Tree leaf = treePath.getLeaf();
        if (treePath.getCompilationUnit() instanceof JCTree.JCCompilationUnit && leaf instanceof JCTree) {
            JCTree.JCCompilationUnit jccu = (JCTree.JCCompilationUnit) treePath.getCompilationUnit();
            java.util.List<JCTree> trees = new ArrayList<>();
            trees.addAll(jccu.defs);
            JCTree.JCIdent ident = treeMaker.Ident(names.fromString("java.util"));
            JCTree.JCImport jcImport = treeMaker.Import(treeMaker.Select(
                    ident, names.fromString("*")), false);
            JCTree.JCIdent ident1 = treeMaker.Ident(names.fromString("com.alibaba.fastjson"));
            JCTree.JCImport jcImport1 = treeMaker.Import(treeMaker.Select(
                    ident1, names.fromString("JSONObject")), false);
            if (!trees.contains(jcImport)) {
                trees.add(0, jcImport);
            }
            if (!trees.contains(jcImport1)) {
                trees.add(1, jcImport1);
            }
            jccu.defs = com.sun.tools.javac.util.List.from(trees);
        }
    }


}
