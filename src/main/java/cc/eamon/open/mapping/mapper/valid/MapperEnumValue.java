package cc.eamon.open.mapping.mapper.valid;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperEnumValue {
    String[] value() default {};

    String enumClass();

    String enumMethod();

}
