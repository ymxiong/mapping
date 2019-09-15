package cc.eamon.open.mapping.mapper.structure.item;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-14 11:32:56
 */
public class MapperField {

    private String simpleName;

    private String mapperName;

    private String qualifiedTypeName;

    private Map<String, MapperStrategy> strategies;

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getQualifiedTypeName() {
        return qualifiedTypeName;
    }

    public void setQualifiedTypeName(String qualifiedTypeName) {
        this.qualifiedTypeName = qualifiedTypeName;
    }

    public Map<String, MapperStrategy> getStrategies() {
        return strategies;
    }

    public void setStrategies(Map<String, MapperStrategy> strategies) {
        this.strategies = strategies;
    }
}
