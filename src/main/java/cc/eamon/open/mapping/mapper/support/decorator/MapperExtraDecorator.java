package cc.eamon.open.mapping.mapper.support.decorator;

import cc.eamon.open.mapping.mapper.structure.decorator.builder.TypeBuilder;
import cc.eamon.open.mapping.mapper.structure.decorator.MapperTypeDecorator;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.alibaba.fastjson.JSONObject;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: Lucas
 * Email: 1181370590@qq.com
 * Time: 2019-12-09 19:58:38
 */
public class MapperExtraDecorator extends MapperTypeDecorator {


    public MapperExtraDecorator(TypeBuilder typeBuilder) {
        super(typeBuilder);
    }

    @Override
    public void decorate() {
        MapperType type=typeBuilder.getMapperType();
        ClassName self=typeBuilder.getSelf();
        TypeSpec.Builder typeSpec=typeBuilder.getTypeSpec();
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


                if (field.getList()) {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            ClassUtils.getParameterizedList(TypeName.get(modifyStrategy.getModifyType())),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    buildMapExtraStaticMethodSpec.addParameter(ClassUtils.getParameterizedList(TypeName.get(field.getType())), renameStrategy.getName());
                    buildSerialMapExtraStaticMethodSpec.addParameter(ClassUtils.getParameterizedList(TypeName.get(field.getType())), renameStrategy.getName());
                } else {
                    FieldSpec.Builder fieldSpec = FieldSpec.builder(
                            TypeName.get(modifyStrategy.getModifyType()),
                            renameStrategy.getName(),
                            Modifier.PUBLIC);
                    typeSpec.addField(fieldSpec.build());
                    buildMapExtraStaticMethodSpec.addParameter(TypeName.get(field.getType()), renameStrategy.getName());
                    buildSerialMapExtraStaticMethodSpec.addParameter(TypeName.get(field.getType()), renameStrategy.getName());
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
}
