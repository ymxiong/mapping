package cc.eamon.open.mapping.ast.item;

import cc.eamon.open.mapping.mapper.structure.item.MapperField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lzr
 * @date 2021/3/6 15:53
 */
public class MapperFieldAST {

    private MapperField mapperField;

    private Boolean isAdded = false;

    private Boolean isExtra = false;

    public MapperFieldAST(MapperField mapperField){
        this.mapperField = mapperField;
    }

    public static List<MapperFieldAST> buildFieldAST(List<MapperField> mapperFields){
        List<MapperFieldAST> fieldASTS = new ArrayList<>();
        mapperFields.forEach(field -> {
            fieldASTS.add(new MapperFieldAST(field));
        });

        return fieldASTS;
    }

    public MapperField getMapperField() {
        return mapperField;
    }

    public void setMapperField(MapperField mapperField) {
        this.mapperField = mapperField;
    }

    public Boolean getExtra() {
        return isExtra;
    }

    public void setExtra(Boolean extra) {
        isExtra = extra;
    }

    public Boolean getAdded() {
        return isAdded;
    }

    public void setAdded(Boolean added) {
        isAdded = added;
    }
}
