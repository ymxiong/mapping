package cc.eamon.open.mapping.mapper.support.pipeline.extra;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class BuildMapSerialExtraStaticPipeline extends BasePipeline {

    private MethodSpec.Builder buildSerialMapExtraStaticMethodSpec;

    public BuildMapSerialExtraStaticPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        logger.info("Mapping build init buildMapSerialExtra:" + type.getQualifiedName());
        String buildMapSerialExtraStaticMethod = "buildSerialMapExtra";
        buildSerialMapExtraStaticMethodSpec = MethodSpec.methodBuilder(buildMapSerialExtraStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(ClassUtils.getParameterizedStringMap());

        buildSerialMapExtraStaticMethodSpec.addStatement("Map<String, String> map = buildSerialMap(obj)");
        buildSerialMapExtraStaticMethodSpec.addStatement("if (obj == null) return map");
        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {
        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());

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
            TypeName fieldTypeNameList = ClassUtils.getParameterizedList(fieldTypeName);
            buildSerialMapExtraStaticMethodSpec.addParameter(fieldTypeNameList, renameStrategy.getName());
        } else {
            buildSerialMapExtraStaticMethodSpec.addParameter(fieldTypeName, renameStrategy.getName());
        }

        buildSerialMapExtraStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", $T.toJSONString(" + renameStrategy.getName() + "))", JSONObject.class);

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        buildSerialMapExtraStaticMethodSpec.addStatement("return map");
        typeSpec.addMethod(buildSerialMapExtraStaticMethodSpec.build());
        return typeSpec;
    }
}
