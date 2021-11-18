package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.pipeline.Pipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.convert.BaseConvertPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.convert.ConvertABPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.convert.ConvertBAPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.convert.InitMapperConvertPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.BuildMapExtraStaticPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.BuildMapSerialExtraStaticPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.extra.InitMapperExtraPipeline;
import cc.eamon.open.mapping.mapper.support.pipeline.mapper.*;
import cc.eamon.open.mapping.mapper.support.strategy.ConvertStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.ExtraStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.IgnoreStrategy;
import cc.eamon.open.mapping.mapper.support.strategy.RenameStrategy;
import cc.eamon.open.mapping.mapper.util.MapperUtils;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Element;
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
        mapperPipeline = new ValidMapperPipeline(mapperPipeline);
        mapperPipeline = new ConstructorPipeline(mapperPipeline);
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

        // build convert
        typeSpec = buildConvert(type, typeSpec);

        return typeSpec.build();

    }

    private static TypeSpec.Builder buildExtra(MapperType type, TypeSpec.Builder typeSpec) {
        // init extra
        ExtraStrategy extraStrategy = (ExtraStrategy) type.getStrategies().get(MapperEnum.EXTRA.getName());
        if (!extraStrategy.open()) return typeSpec;

        // init pipeline
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

    private static TypeSpec.Builder buildConvert(MapperType type, TypeSpec.Builder typeSpec) {
        // init convert
        ConvertStrategy convertStrategy = (ConvertStrategy) type.getStrategies().get(MapperEnum.CONVERT.getName());
        if (!convertStrategy.open()) return typeSpec;

        // init pipeline
        BaseConvertPipeline mapperConvertPipeline = new InitMapperConvertPipeline();
        mapperConvertPipeline = new ConvertABPipeline(mapperConvertPipeline);
        mapperConvertPipeline = new ConvertBAPipeline(mapperConvertPipeline);

        logger.debug("Mapping build init convert:" + type.getQualifiedName());
        for (TypeMirror convertStrategyType : convertStrategy.getTypes()) {
            Map<String, TypeMirror> fieldTypeMirrors = new HashMap<>();
            List<Element> elements = MapperUtils.loadTypeEnclosedElements(convertStrategyType);
            elements.forEach(element -> fieldTypeMirrors.put(element.getSimpleName().toString(), element.asType()));

            mapperConvertPipeline.setConvertStrategyType(convertStrategyType);
            mapperConvertPipeline.setFieldTypeMirrors(fieldTypeMirrors);

            // build before
            typeSpec = mapperConvertPipeline.buildBefore(type, typeSpec);
            for (MapperField field : type.getMapperFieldList()) {
                IgnoreStrategy ignoreStrategy = (IgnoreStrategy) field.getStrategies().get(MapperEnum.IGNORE.getName());
                RenameStrategy renameStrategy = (RenameStrategy) field.getStrategies().get(MapperEnum.RENAME.getName());

                if (fieldTypeMirrors.get(renameStrategy.getName()) == null) {
                    continue;
                }

                if (ignoreStrategy.ignore()) {
                    continue;
                }

                mapperConvertPipeline.buildField(field, null);
            }
            typeSpec = mapperConvertPipeline.buildAfter(type, typeSpec);
        }
        return typeSpec;
    }

}
