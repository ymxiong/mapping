package cc.eamon.open.mapping.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Eamon on 2018/9/29.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperRename {

    String[] value() default {};

    String[] name() default {};

}
