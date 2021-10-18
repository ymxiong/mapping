package cc.eamon.open.mapping.mapper.support.pipeline.convert.strategy;

import cc.eamon.open.mapping.mapper.Mapper;
import cc.eamon.open.mapping.mapper.structure.factory.MapperFactory;
import cc.eamon.open.mapping.mapper.support.factory.BasicMapperFactory;

import java.lang.annotation.Annotation;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2021-10-18 21:01:41
 */
public enum ConvertStrategyEnum {


    BigDecimal2Long("BigDecimal2Long", "java.math.BigDecimal", "java.lang.Long", new BigDecimal2LongStrategy()),

    BigDecimal2Integer("BigDecimal2Integer", "java.math.BigDecimal", "java.lang.Integer", new BigDecimal2IntegerStrategy()),

    Exception("Exception", "Exception", "Exception", new ExceptionStrategy()),

    Default("Default", "Default", "Default", new DefaultStrategy()),

    ;

    private String name;

    private String from;

    private String to;

    private ConvertStrategy strategy;

    ConvertStrategyEnum(String name, String from, String to, ConvertStrategy strategy) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.strategy = strategy;
    }

    public static ConvertStrategyEnum getByKey(String from, String to) {
        if (from.equals(to)) return ConvertStrategyEnum.Default;
        for (ConvertStrategyEnum value : ConvertStrategyEnum.values()) {
            if (value.getFrom().equals(from) && value.getTo().equals(to)) {
                return value;
            }
        }
        return ConvertStrategyEnum.Exception;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public ConvertStrategy getStrategy() {
        return strategy;
    }
}
