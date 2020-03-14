package cc.eamon.open.mapping.mapper.support.pipeline.extra;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.BasePipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class BuildMapExtraStaticPipeline extends BasePipeline {

    private MethodSpec.Builder buildMapExtraStaticMethodSpec;

    public BuildMapExtraStaticPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        logger.info("Mapping build init buildMapExtra:" + type.getQualifiedName());
        String buildMapExtraStaticMethod = "buildMapExtra";
        buildMapExtraStaticMethodSpec = MethodSpec.methodBuilder(buildMapExtraStaticMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "obj")
                .returns(ClassUtils.getParameterizedObjectMap());

        buildMapExtraStaticMethodSpec.addStatement("Map<String, Object> map = buildMap(obj)");
        buildMapExtraStaticMethodSpec.addStatement("if (obj == null) return map");
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
            buildMapExtraStaticMethodSpec.addParameter(fieldTypeNameList, renameStrategy.getName());
        } else {
            buildMapExtraStaticMethodSpec.addParameter(fieldTypeName, renameStrategy.getName());
        }

        buildMapExtraStaticMethodSpec.addStatement("map.put(\"" + renameStrategy.getName() + "\", " + renameStrategy.getName() + ")");

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        buildMapExtraStaticMethodSpec.addStatement("return map");
        typeSpec.addMethod(buildMapExtraStaticMethodSpec.build());
        return typeSpec;
    }
}
