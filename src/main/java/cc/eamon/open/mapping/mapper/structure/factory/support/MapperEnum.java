package cc.eamon.open.mapping.mapper.structure.factory.support;

import cc.eamon.open.mapping.mapper.*;
import cc.eamon.open.mapping.mapper.structure.factory.MapperFactory;

import java.lang.annotation.Annotation;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-08-31 22:47:27
 */
public enum MapperEnum {

    EXTRA("EXTRA", MapperExtra.class, new MapperExtraFactory()),

    DOC("DOC", MapperDoc.class, new MapperDocFactory()),

    IGNORE("IGNORE", MapperIgnore.class, new MapperIgnoreFactory()),

    MODIFY("MODIFY", MapperModify.class, new MapperModifyFactory()),

    RENAME("RENAME", MapperRename.class, new MapperRenameFactory());

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
