package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.pipeline.*;
import cc.eamon.open.mapping.mapper.support.strategy.*;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
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

        Pipeline pipeline = new InitPipeline();
        pipeline = new BuildMapperStaticPipeline(pipeline);
        pipeline = new BuildMapPipeline(pipeline);
        pipeline = new BuildMapStaticPipeline(pipeline);
        pipeline = new BuildSerialMapStaticPipeline(pipeline);
        pipeline = new BuildEntityPipeline(pipeline);
        pipeline = new ParseEntityPipeline(pipeline);
        pipeline = new ParseSerialEntityPipeline(pipeline);

        // build before
        TypeSpec.Builder typeSpec = pipeline.buildBefore(type, null);

        // build fields
        for (MapperField field : type.getMapperFieldList()) {
            IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());

            if (ignoreStrategy.ignore()) {
                continue;
            }

            // build field
            FieldSpec.Builder fieldSpec = pipeline.buildField(field, null);
            typeSpec.addField(fieldSpec.build());
        }

        // build after
        typeSpec = pipeline.buildAfter(type, typeSpec);

        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        // add method
        buildExtra(type, typeSpec, self);
        buildConvert(type, typeSpec, self);

        return typeSpec.build();

    }

    private static void buildExtra(MapperType type, TypeSpec.Builder typeSpec, ClassName self) {
        // init extra
        ExtraStrategy extraStrategy = (ExtraStrategy) type.getStrategies().get(MapperEnum.EXTRA.getName());
        if (extraStrategy.open()) {

            logger.info("Mapping build init buildMapExtra:" + type.getQualifiedName());
            String buildMapExtraStaticMethod = "buildMapExtra";
            MethodSpec.Builder buildMapExtraStaticMethodSpec = MethodSpec.methodBuilder(buildMapExtraStaticMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .addParameter(self, "obj")
                    .returns(ClassUtils.getParameterizedObjectMap());

            buildMapExtraStaticMethodSpec.addStatement("Map<String, Object> map = buildMap(obj)");
            buildMapExtraStaticMethodSpec.addStatement("if (obj == null) return map");

            logger.info("Mapping build init buildMapSerialExtra:" + type.getQualifiedName());
            String buildMapSerialExtraStaticMethod = "buildSerialMapExtra";
            MethodSpec.Builder buildSerialMapExtraStaticMethodSpec = MethodSpec.methodBuilder(buildMapSerialExtraStaticMethod)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .addParameter(self, "obj")
                    .returns(ClassUtils.getParameterizedStringMap());

            buildSerialMapExtraStaticMethodSpec.addStatement("Map<String, String> map = buildSerialMap(obj)");
            buildSerialMapExtraStaticMethodSpec.addStatement("if (obj == null) return map");

            for (MapperField field : extraStrategy.getMapperFields()) {
                RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
                ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

                ClassName[] typeArgsClassName = null;
                if (field.getTypeArgs() != null) {
                    typeArgsClassName = new ClassName[field.getTypeArgs().length];
                    for (int i = 0; i < field.getTypeArgs().length; i++) {
                        typeArgsClassName[i] = ClassUtils.getTargetClassType(field.getTypeArgs()[i]);
                    }
                }

                TypeName fieldTypeName = TypeName.get(field.getType());
                if (typeArgsClassName != null) {
                    fieldTypeName = ClassUtils.getParameterizedType((ClassName) fieldTypeName, typeArgsClassName);
                }

                if (field.getList()) {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            ClassUtils.getParameterizedList(TypeName.get(modifyStrategy.getModifyType())),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    TypeName fieldTypeNameList = ClassUtils.getParameterizedList(fieldTypeName);
                    buildMapExtraStaticMethodSpec.addParameter(fieldTypeNameList, renameStrategy.getName());
                    buildSerialMapExtraStaticMethodSpec.addParameter(fieldTypeNameList, renameStrategy.getName());
                } else {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            TypeName.get(modifyStrategy.getModifyType()),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    buildMapExtraStaticMethodSpec.addParameter(fieldTypeName, renameStrategy.getName());
                    buildSerialMapExtraStaticMethodSpec.addParameter(fieldTypeName, renameStrategy.getName());
                }

                buildMapExtraStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", " + renameStrategy.getName() + ")");
                buildSerialMapExtraStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", $T.toJSONString(" + renameStrategy.getName() + "))", JSONObject.class);

            }
            buildMapExtraStaticMethodSpec.addStatement("return map");
            buildSerialMapExtraStaticMethodSpec.addStatement("return map");
            typeSpec.addMethod(buildMapExtraStaticMethodSpec.build());
            typeSpec.addMethod(buildSerialMapExtraStaticMethodSpec.build());
        }
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
