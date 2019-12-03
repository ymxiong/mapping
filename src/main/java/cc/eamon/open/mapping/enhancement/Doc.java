package cc.eamon.open.mapping.enhancement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-01 23:46:45
 */
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Doc {

    String note() default "";

}

