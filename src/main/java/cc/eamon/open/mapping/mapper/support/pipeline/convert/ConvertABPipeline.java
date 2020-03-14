package cc.eamon.open.mapping.mapper.support.pipeline.convert;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.strategy.ModifyStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.StringUtils;
import com.squareup.javapoet.*;

import javax.lang.model.element.Modifier;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2020-03-15 02:39:56
 */
public class ConvertABPipeline extends BaseConvertPipeline {

    private MethodSpec.Builder buildConvertAB;

    public ConvertABPipeline(Pipeline pipeline) {
        super(pipeline);
    }

    @Override
    public TypeSpec.Builder buildTypeBefore(MapperType type, TypeSpec.Builder typeSpec) {
        // define import items
        ClassName self = ClassName.get(type.getPackageName(), type.getSimpleName());

        String convertMethod = "convert";
        buildConvertAB = MethodSpec.methodBuilder(convertMethod)
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.STATIC)
                .addParameter(self, "from")
                .addParameter(TypeName.get(getConvertStrategyType()), "to")
                .returns(TypeName.get(getConvertStrategyType()));
        buildConvertAB.addStatement("if (from == null || to == null) return to");
        return typeSpec;
    }

    @Override
    public FieldSpec.Builder buildSelfField(MapperField field, FieldSpec.Builder fieldSpec) {

        RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());
        ModifyStrategy modifyStrategy = (ModifyStrategy) field.getStrategies().get(MapperEnum.MODIFY.getName());

        String fieldUpperCase = StringUtils.firstWordToUpperCase(renameStrategy.getName());

        if (!modifyStrategy.getModifyType().toString().equals(getFieldTypeMirrors().get(renameStrategy.getName()).toString())) {
            logger.warn("Mapping build convert type not fit, try to convert:" + modifyStrategy.getModifyType().toString() + " to " + getFieldTypeMirrors().get(renameStrategy.getName()));
            String fixme = "// FIXME: type[" + renameStrategy.getElementName() + "] do not fit " + getFieldTypeMirrors().toString() + "[" + renameStrategy.getName() + "]";
            buildConvertAB.addStatement(fixme);
            buildConvertAB.addStatement("// to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ")");
            return fieldSpec;
        }

        buildConvertAB.addStatement("to.set" + fieldUpperCase + "(" + modifyStrategy.getModifyName("from") + ")");

        return fieldSpec;
    }

    @Override
    public TypeSpec.Builder buildTypeAfter(MapperType type, TypeSpec.Builder typeSpec) {

        buildConvertAB.addStatement("return to");
        typeSpec.addMethod(buildConvertAB.build());

        return typeSpec;
    }
}
