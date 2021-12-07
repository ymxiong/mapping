package cc.eamon.open.mapping.mapper.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperEnumValue {
    String[] value() default {};

    String message() default "参数值与枚举值不匹配";

    String enumClass();


    String enumMethod() default "contains";

}
