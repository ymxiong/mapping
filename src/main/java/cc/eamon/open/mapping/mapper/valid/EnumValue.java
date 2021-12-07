package cc.eamon.open.mapping.mapper.valid;


import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValue.Validator.class)
public @interface EnumValue {

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String enumClass();

    String message() default "参数值与枚举值不匹配";

    String enumMethod() default "contains";



    class Validator implements ConstraintValidator<EnumValue, Object> {

        private Class<? extends Enum<?>> enumClass;
        private String enumMethod;

        @Override
        public void initialize(EnumValue enumValue) {
            enumMethod = enumValue.enumMethod();
            try {
                enumClass = (Class<? extends Enum<?>>) Class.forName(enumValue.enumClass());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value == null) {
                return Boolean.TRUE;
            }

            if (enumClass == null || enumMethod == null) {
                return Boolean.TRUE;
            }

            Class<?> valueClass = value.getClass();

            try {
                Method method = enumClass.getMethod(enumMethod, valueClass);
                if (!Boolean.TYPE.equals(method.getReturnType()) && !Boolean.class.equals(method.getReturnType())) {
                    throw new RuntimeException(String.format("%s method return is not boolean type in the %s class", enumMethod, enumClass));
                }

                if(!Modifier.isStatic(method.getModifiers())) {
                    throw new RuntimeException(String.format("%s method is not static method in the %s class", enumMethod, enumClass));
                }

                Boolean result = (Boolean)method.invoke(null, value);
                return result == null ? false : result;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new RuntimeException(String.format("This %s(%s) method does not exist in the %s", enumMethod, valueClass, enumClass), e);
            }
        }

    }
}
