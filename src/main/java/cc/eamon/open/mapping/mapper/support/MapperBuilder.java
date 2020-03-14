package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.BuildMapExtraStaticPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.BuildMapSerialExtraStaticPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.InitMapperExtraPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.mapper.*;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.squareup.javapoet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeMirror;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 18:21:02
 */
public class MapperBuilder {

    private static Logger logger = LoggerFactory.getLogger(MapperBuilder.class);

    public static TypeSpec build(MapperType type) {

        Pipeline mapperPipeline = new InitMapperPipeline();
        mapperPipeline = new BuildMapperStaticPipeline(mapperPipeline);
        mapperPipeline = new BuildMapPipeline(mapperPipeline);
        mapperPipeline = new BuildMapStaticPipeline(mapperPipeline);
        mapperPipeline = new BuildSerialMapStaticPipeline(mapperPipeline);
        mapperPipeline = new BuildEntityPipeline(mapperPipeline);
        mapperPipeline = new ParseEntityPipeline(mapperPipeline);
        mapperPipeline = new ParseSerialEntityPipeline(mapperPipeline);

        // build before
        TypeSpec.Builder typeSpec = mapperPipeline.buildBefore(type, null);

        // build fields
        for (MapperField field : type.getMapperFieldList()) {
            IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());

            if (ignoreStrategy.ignore()) {
                continue;
            }

            // build field
            FieldSpec.Builder fieldSpec = mapperPipeline.buildField(field, null);
            typeSpec.addField(fieldSpec.build());
        }

        // build after
        typeSpec = mapperPipeline.buildAfter(type, typeSpec);

        // build extra
        typeSpec = buildExtra(type, typeSpec);

        // TODO: START REFACTOR
        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());
        buildConvert(type, typeSpec, self);
        // TODO: END REFACTOR
        return typeSpec.build();

    }

    private static TypeSpec.Builder buildExtra(MapperType type, TypeSpec.Builder typeSpec) {
        // init extra
        ExtraStrategy extraStrategy = (ExtraStrategy) type.getStrategies().get(MapperEnum.EXTRA.getName());
        if (!extraStrategy.open()) return typeSpec;
        Pipeline mapperExtraPipeline = new InitMapperExtraPipeline();
        mapperExtraPipeline = new BuildMapExtraStaticPipeline(mapperExtraPipeline);
        mapperExtraPipeline = new BuildMapSerialExtraStaticPipeline(mapperExtraPipeline);

        // build Before
        typeSpec = mapperExtraPipeline.buildBefore(type, typeSpec);

        for (MapperField field : extraStrategy.getMapperFields()) {
            FieldSpec.Builder fieldSpec = mapperExtraPipeline.buildField(field, null);
            typeSpec.addField(fieldSpec.build());
        }

        // build After
        typeSpec = mapperExtraPipeline.buildAfter(type, typeSpec);
        return typeSpec;
    }

    private static void buildConvert(MapperType type, TypeSpec.Builder typeSpec, ClassName self) {
        // init convert
        ConvertStrategy convertStrategy = (ConvertStrategy) type.getStrategies().get(MapperEnum.CONVERT.getName());

        if (convertStrategy.open()) {

            logger.info("Mapping build init convert:" + type.getQualifiedName());
            String convertMethod = "convert";
            for (TypeMirror convertStrategyType : convertStrategy.getTypes()) {

                Map<String, TypeMirror> fieldTypeMirrors = new HashMap<>();
                List<Element> elements = MapperUtils.loadTypeEnclosedElements(convertStrategyType);
                elements.forEach(element -> fieldTypeMirrors.put(element.getSimpleName().toString(), element.asType()));

                MethodSpec.Builder buildConvertAB = MethodSpec.methodBuilder(convertMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .addParameter(self, "from")
                        .addParameter(TypeName.get(convertStrategyType), "to")
                        .returns(TypeName.get(convertStrategyType));
                buildConvertAB.addStatement("if (from == null || to == null) return to");

                MethodSpec.Builder buildConvertBA = MethodSpec.methodBuilder(convertMethod)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.STATIC)
                        .addParameter(TypeName.get(convertStrategyType), "from")
                        .addParameter(self, "to")
                        .returns(self);
                buildConvertBA.addStatement("if (from == null || to == null) return to");

                for (MapperField field : type.getMapperFieldList()) {
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
                        String fixme = "// FIXME: " + type.getQualifiedName() + "[" + renameStrategy.getElementName() + "] do not fit " + convertStrategyType.toString() + "[" + renameStrategy.getName() + "]";
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

                typeSpec.addMethod(buildConvertAB.build());
                typeSpec.addMethod(buildConvertBA.build());
            }

        }

    }

}
