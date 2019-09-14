package cc.eamon.open.mapping.mapper.structure.item;

import cc.eamon.open.mapping.mapper.structure.strategy.MapperStrategy;

import java.util.List;
import java.util.Map;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-07-14 11:32:56
 */
public class MapperType {

    private String packageName;

    private String simpleName;

    private String qualifiedName;

    private String mapperName;

    private List<MapperMethod> mapperMethodList;

    private List<MapperField> mapperFieldList;

    private Map<String, MapperStrategy> strategies;

    private MapperType() {
    }

    private MapperType(
            String packageName,
            String simpleName,
            String qualifiedName,
            String mapperName,
            List<MapperMethod> mapperMethodList,
            List<MapperField> mapperFieldList,
            Map<String, MapperStrategy> strategies
    ) {
        this.packageName = packageName;
        this.simpleName = simpleName;
        this.qualifiedName = qualifiedName;
        this.mapperName = mapperName;
        this.mapperMethodList = mapperMethodList;
        this.mapperFieldList = mapperFieldList;
        this.strategies = strategies;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public List<MapperMethod> getMapperMethodList() {
        return mapperMethodList;
    }

    public List<MapperField> getMapperFieldList() {
        return mapperFieldList;
    }

    public Map<String, MapperStrategy> getStrategies() {
        return strategies;
    }

    public static MapperType.MapperTypeBuilder builder() {
        return new MapperType.MapperTypeBuilder();
    }

    public static class MapperTypeBuilder {

        private String packageName;

        private String simpleName;

        private String qualifiedName;

        private String mapperName;

        private List<MapperMethod> mapperMethodList;

        private List<MapperField> mapperFieldList;

        private Map<String, MapperStrategy> strategies;

        private MapperTypeBuilder() {
        }

        public MapperTypeBuilder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public MapperTypeBuilder simpleName(String simpleName) {
            this.simpleName = simpleName;
            return this;
        }

        public MapperTypeBuilder qualifiedName(String qualifiedName) {
            this.qualifiedName = qualifiedName;
            return this;
        }

        public MapperTypeBuilder mapperName(String mapperName) {
            this.mapperName = mapperName;
            return this;
        }

        public MapperTypeBuilder mapperMethodList(List<MapperMethod> mapperMethodList) {
            this.mapperMethodList = mapperMethodList;
            return this;
        }

        public MapperTypeBuilder mapperFieldList(List<MapperField> mapperFieldList) {
            this.mapperFieldList = mapperFieldList;
            return this;
        }

        public MapperTypeBuilder mapperStrategies(Map<String, MapperStrategy> strategies) {
            this.strategies = strategies;
            return this;
        }


        public MapperType build() {
            return new MapperType(
                    packageName,
                    simpleName,
                    qualifiedName,
                    mapperName,
                    mapperMethodList,
                    mapperFieldList,
                    strategies);
        }
    }

}
