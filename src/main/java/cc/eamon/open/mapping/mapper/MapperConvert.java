package cc.eamon.open.mapping.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-09-15 18:46:29
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperConvert {

    String[] value() default {};

    Class<?>[] type() default {};

}
