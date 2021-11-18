package cc.eamon.open.mapping.mapper.support;

import cc.eamon.open.mapping.mapper.*;
import cc.eamon.open.mapping.mapper.structure.factory.MapperFactory;
import cc.eamon.open.mapping.mapper.support.factory.*;
import cc.eamon.open.mapping.mapper.valid.*;

import java.lang.annotation.Annotation;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-08-31 22:47:27
 */
public enum MapperEnum {

    MAPPER("MAPPER", Mapper.class, new BasicMapperFactory()),

    EXTENDS("EXTENDS", Mapper.class, new MapperExtendsFactory()),

    EXTRA("EXTRA", MapperExtra.class, new MapperExtraFactory()),

    DOC("DOC", MapperDoc.class, new MapperDocFactory()),

    IGNORE("IGNORE", MapperIgnore.class, new MapperIgnoreFactory()),

    CONSTRUCTORIGNORE("CONSTRUCTORIGNORE", MapperConstructorIgnore.class, new MapperConstructorIgnoreFactory()),

    MODIFY("MODIFY", MapperModify.class, new MapperModifyFactory()),

    RENAME("RENAME", MapperRename.class, new MapperRenameFactory()),

    DEFAULTVALUE("DEFAULTVALUE", MapperDefaultValue.class, new MapperDefaultValueFactory()),

    CONVERT("CONVERT", MapperConvert.class, new MapperConvertFactory()),

    NOTNULL("NOTNULL", MapperNotNull.class, new MapperNotNullFactory()),

    NULL("NULL", MapperNull.class, new MapperNullFactory()),

    NOTBLANK("NOTBLANK", MapperNotBlank.class, new MapperNotBlankFactory()),

    NOTEMPTY("NOTEMPTY", MapperNotEmpty.class, new MapperNotEmptyFactory()),

    MAX("MAX", MapperMax.class, new MapperMaxFactory()),

    MIN("MIN", MapperMin.class, new MapperMinFactory());


    private String name;

    private Class<? extends Annotation> type;

    private MapperFactory factory;

    MapperEnum(String name, Class<? extends Annotation> type, MapperFactory factory) {
        this.name = name;
        this.type = type;
        this.factory = factory;
    }

    public String getName() {
        return name;
    }

    public Class<? extends Annotation> getType() {
        return type;
    }

    public MapperFactory getFactory() {
        return factory;
    }
}
