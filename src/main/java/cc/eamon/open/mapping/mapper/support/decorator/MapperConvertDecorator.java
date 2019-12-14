package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.IgnoreStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author:Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperConvertDecorator extends MapperTypeDecorator {


    public MapperConvertDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public void decorate() {

        // init convert
        ConvertStrategy convertStrategy = (ConvertStrategy) typeBuilder.getMapperType().getStrategies().get(MapperEnum.CONVERT.getName());

        if (convertStrategy.open()) {

            logger.info("Mapping build init convert:" + typeBuilder.getMapperType().getQualifiedName());
            String convertMethod = "convert";
            for (TypeMirror convertStrategyType : convertStrategy.getTypes()) {

                Map<String, TypeMirror> fieldTypeMirrors = new HashMap<>();
                List<Element> elements = MapperUtils.loadTypeEnclosedElements(convertStrategyType);
                elements.forEach(element -> fieldTypeMirrors.put(element.getSimpleName().toString(), element.asType()));

                MethodSpec.Builder buildConvertAB = MethodSpec.methodBuilder(convertMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .addParameter(typeBuilder.getSelf(), "from")
                        .addParameter(TypeName.get(convertStrategyType), "to")
                        .returns(TypeName.get(convertStrategyType));
                buildConvertAB.addStatement("if (from == null || to == null) return to");

                MethodSpec.Builder buildConvertBA = MethodSpec.methodBuilder(convertMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .addParameter(TypeName.get(convertStrategyType), "from")
                        .addParameter(typeBuilder.getSelf(), "to")
                        .returns(typeBuilder.getSelf());
                buildConvertBA.addStatement("if (from == null || to == null) return to");

                for (MapperField field : typeBuilder.getMapperType().getMapperFieldList()) {
                    IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());
                    RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
                    ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

                    String fieldUpperCase = StringUtils.firstWordToUpperCase(renameStrategy.getName());

                    if (fieldTypeMirrors.get(renameStrategy.getName()) == null) {
                        continue;
                    }

                    if (ignoreStrategy.ignore()) {
                        continue;
                    }

                    if (!modifyStrategy.getModifyType().toString().equals(fieldTypeMirrors.get(renameStrategy.getName()).toString())) {
                        logger.warn("Mapping build convert type not fit, try to convert:" + modifyStrategy.getModifyType().toString() + " to " + fieldTypeMirrors.get(renameStrategy.getName()));
                        String fixme = "// FIXME: " + typeBuilder.getMapperType().getQualifiedName() + "[" + renameStrategy.getElementName() + "] do not fit " + convertStrategyType.toString() + "[" + renameStrategy.getName() + "]";
                        buildConvertAB.addStatement(fixme);
                        buildConvertAB.addStatement("// to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ")");
                        buildConvertBA.addStatement(fixme);
                        buildConvertBA.addStatement("// " + modifyStrategy.getRecoverName("to").replace("$", "from.get" + fieldUpperCase + "()"));
                        continue;
                    }

                    buildConvertAB.addStatement("to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ")");
                    buildConvertBA.addStatement(modifyStrategy.getRecoverName("to").replace("$", "from.get" + fieldUpperCase + "()"));
                }
                buildConvertAB.addStatement("return to");
                buildConvertBA.addStatement("return to");

                typeBuilder.getTypeSpec().addMethod(buildConvertAB.build());
                typeBuilder.getTypeSpec().addMethod(buildConvertBA.build());

            }
        }
    }
}
