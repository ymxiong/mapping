package cc.eamon.open.mapping.mapper.support.strategy;

import cc.eamon.open.mapping.mapper.util.StringUtils;
import cc.eamon.open.mapping.mapper.structure.item.MapperField;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperBaseStrategy;
import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;
import cc.eamon.open.mapping.mapper.support.MapperEnum;
import cc.eamon.open.mapping.mapper.support.detail.ExtraDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-14 16:53:17
 */
public class ExtraEnableStrategy extends MapperBaseStrategy implements ExtraStrategy {

    private List<ExtraDetail> details;

    public List<ExtraDetail> getDetails() {
        return details;
    }

    public void setDetails(List<ExtraDetail> details) {
        this.details = details;
    }

    @Override
    public boolean open() {
        return true;
    }

    @Override
    public List<MapperField> getMapperFields() {
        List<MapperField> fields = new ArrayList<>();
        details.forEach((detail) -> {
            MapperField field = new MapperField();
            field.setMapperName(detail.getMapper());
            field.setSimpleName(StringUtils.classNameFromQualifiedName(detail.getName()));
            field.setType(detail.getType());
            field.setList(detail.getList());

            Map<String, MapperStrategy> strategies = new HashMap<>();
            field.setStrategies(strategies);
            MapperBaseStrategy ignoreStrategy = new IgnoreNormalStrategy();
            MapperBaseStrategy modifyStrategy = new ModifyNormalStrategy();
            MapperBaseStrategy renameStrategy = new RenameNormalStrategy();
            ignoreStrategy.setElementName(field.getSimpleName());
            modifyStrategy.setElementName(field.getSimpleName());
            renameStrategy.setElementName(field.getSimpleName());
            ignoreStrategy.setMapper(detail.getMapper());
            modifyStrategy.setMapper(detail.getMapper());
            renameStrategy.setMapper(detail.getMapper());
            ignoreStrategy.setType(field.getType());
            modifyStrategy.setType(field.getType());
            renameStrategy.setType(field.getType());
            strategies.put(MapperEnum.IGNORE.getName(), ignoreStrategy);
            strategies.put(MapperEnum.MODIFY.getName(), modifyStrategy);
            strategies.put(MapperEnum.RENAME.getName(), renameStrategy);
            fields.add(field);
        });
        return fields;
    }
}
