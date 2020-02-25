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
public @interface MapperExtra {

    String[] value() default {};

    String[] name() default {};

    Class<?>[] type() default {};

    String[] typeArgs() default {};

    boolean[] list() default {};

}
