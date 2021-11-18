package cc.eamon.open.mapping.mapper.valid;

import javax.validation.constraints.Min;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperMax {
    String[] value() default {};

    String[] message() default {};

    long[] maxValue() default {};


}
