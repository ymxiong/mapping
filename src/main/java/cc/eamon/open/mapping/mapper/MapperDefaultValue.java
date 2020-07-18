package cc.eamon.open.mapping.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Lucas on 2019/4/2.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperDefaultValue {

    String[] value() default {};

    String[] defaultValue() default {};

}
