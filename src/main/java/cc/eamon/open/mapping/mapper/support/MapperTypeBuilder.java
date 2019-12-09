package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.structure.enums.MethodEnum;
import cc.eamon.open.mapping.mapper.structure.item.MapperType;
import cc.eamon.open.mapping.mapper.support.strategy.BasicMapperStrategy;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 20:24:20
 */
public class MapperTypeBuilder {

    private static Logger logger = LoggerFactory.getLogger(MapperBuilder.class);

    public static TypeSpec build(MapperType type) {
        // mapper strategies
        BasicMapperStrategy basicMapperStrategy = (BasicMapperStrategy) type.getStrategies().get(MapperEnum.MAPPER.getName());

        // define new type
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(basicMapperStrategy.getBuildTypeName()).addModifiers(Modifier.PUBLIC);

        List<MethodSpec.Builder> methodSpecList = new ArrayList<>();
        for (MethodEnum methodEnum : MethodEnum.values()) {
            methodSpecList.add(methodEnum.getMethodBuilder());
        }

        return typeSpec.build();
    }

}
