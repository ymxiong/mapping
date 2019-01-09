package cc.eamon.open.mapping.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Eamon on 2018/9/29.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperGroup {

    boolean[] base() default {};

    boolean[] list() default {};

    String[] value() default {};

    String[] name() default {};

    String[] target() default {};
}
