package cc.eamon.open.mapping.mapper.valid;

import javax.validation.constraints.Email;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.SOURCE)

@Email
public @interface MapperNotEmpty {

    String[] value() default {};

    String[] message() default {};


}
