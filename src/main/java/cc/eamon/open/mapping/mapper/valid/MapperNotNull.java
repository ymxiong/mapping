package cc.eamon.open.mapping.mapper.valid;


import java.lang.annotation.*;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)
public @interface MapperNotNull {

    String[] value() default {};

    String[] message() default {};


}
