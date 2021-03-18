package cc.eamon.open.mapping.ast.item;

import cc.eamon.open.mapping.mapper.structure.item.MapperType;

import java.util.List;

/**
 * @author lzr
 * @date 2021/3/5 8:52
 */
public class MapperInfo{
    private MapperType mapperType;

    private List<MapperFieldAST> mapperFields;

    public MapperInfo(MapperType mapperType){
        this.mapperType = mapperType;
        mapperFields = MapperFieldAST.buildFieldAST(this.mapperType.getMapperFieldList());
    }

    public MapperType getMapperType(){
        return this.mapperType;
    }

    public void setMapperFields(List<MapperFieldAST> mapperFields){
        this.mapperFields = mapperFields;
    }

    public List<MapperFieldAST> getMapperFields(){
        return this.mapperFields;
    }

}
