package cc.eamon.open.mapping.ast.handler.method.convert;

import cc.eamon.open.mapping.ast.handler.method.MethodHandler;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.ListBuffer;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lzr
 * @date 2021/3/13 13:51
 */
public abstract class ConvertMethodHandler extends MethodHandler {

    protected List<String> buildField(List<Element> elements){
        return elements.stream()
                .filter(element -> element.getKind() == ElementKind.FIELD&&!element.getModifiers().contains(Modifier.FINAL)&&!element.getModifiers().contains(Modifier.STATIC))
                .map(element -> element.getSimpleName().toString())
                .collect(Collectors.toList());
    }

    protected ListBuffer<JCTree.JCVariableDecl> createFromToParams(String from, String to){
        ListBuffer<JCTree.JCVariableDecl> params = new ListBuffer<>();
        JCTree.JCVariableDecl fromParam = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("from"),
                treeMaker.Ident(names.fromString(from)),
                null
        );
        JCTree.JCVariableDecl toParam = treeMaker.VarDef(
                treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("to"),
                treeMaker.Ident(names.fromString(to)),
                null
        );
        params.append(fromParam).append(toParam);
        return params;
    }

    protected void createSetAndGet(List<String> fieldNames, ListBuffer<JCTree.JCStatement> statements){
        fieldNames.forEach(fieldName -> {
            JCTree.JCExpression get = treeMaker.Exec(treeMaker.Apply(
                    com.sun.tools.javac.util.List.nil(),
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString("from")),
                            names.fromString("get" + StringUtils.firstWordToUpperCase(fieldName))
                    ),
                    com.sun.tools.javac.util.List.nil()
            )).expr;

            JCTree.JCExpressionStatement set = treeMaker.Exec(treeMaker.Apply(
                    com.sun.tools.javac.util.List.nil(),
                    treeMaker.Select(
                            treeMaker.Ident(names.fromString("to")),
                            names.fromString("set" + StringUtils.firstWordToUpperCase(fieldName))
                    ),
                    com.sun.tools.javac.util.List.of(get)
            ));
            statements.append(set);
        });
    }


    protected void createIfNew(String type, ListBuffer<JCTree.JCStatement> statements){
        JCTree.JCMethodInvocation apply = treeMaker.Apply(
                com.sun.tools.javac.util.List.nil(),
                treeMaker.Select(
                        treeMaker.Ident(names.fromString("Objects")),
                        names.fromString("isNull")
                ),
                com.sun.tools.javac.util.List.of(treeMaker.Ident(names.fromString("to")))
        );
        JCTree.JCExpressionStatement to = treeMaker.Exec(treeMaker.Assign(
                treeMaker.Ident(names.fromString("to")),
                treeMaker.NewClass(null, com.sun.tools.javac.util.List.nil(), treeMaker.Ident(names.fromString(type)), com.sun.tools.javac.util.List.nil(), null)
        ));
        JCTree.JCIf anIf = treeMaker.If(apply, to, null);
        statements.append(anIf);
    }

    public JCTree createConvertMethod(Boolean fromAToB){
        String fromType;
        String toType;
        ConvertStrategy strategy = (ConvertStrategy) this.mapperInfo.getMapperType().getStrategies().get(MapperEnum.CONVERT.getName());
        List<TypeMirror> types = strategy.getTypes();
        TypeMirror typeMirror = types.get(0);
        if(fromAToB){
            fromType = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getSimpleName());
            toType = StringUtils.classNameFromQualifiedName(typeMirror.toString());
        }else{
            fromType = StringUtils.classNameFromQualifiedName(typeMirror.toString());
            toType = StringUtils.firstWordToUpperCase(this.mapperInfo.getMapperType().getSimpleName());
        }
        List<Element> elements = MapperUtils.loadTypeEnclosedElements(typeMirror);
        ListBuffer<JCTree.JCVariableDecl> fromToParams = createFromToParams(fromType, toType);
        ListBuffer<JCTree.JCStatement> statements = new ListBuffer<>();
        createIfReturn(statements, "from", "to");
        createIfNew(toType, statements);
        List<String> fieldNames = buildField(elements);
        createSetAndGet(fieldNames, statements);
        statements.append(treeMaker.Return(treeMaker.Ident(names.fromString("to"))));
        JCTree.JCBlock block = this.treeMaker.Block(0, statements.toList());
        return createReturnObjectMethod(true, toType, "convert", block, fromToParams);
    }
}
