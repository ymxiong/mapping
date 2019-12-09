package cc.eamon.open.mapping.mapper.structure.enums;

import cc.eamon.open.mapping.mapper.util.ClassUtils;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: eamon
 * Email: eamon@eamon.cc
 * Time: 2019-12-03 20:12:00
 */
public enum MethodEnum {

    BUILD_MAP("buildMap", ClassUtils.getParameterizedObjectMap(), Modifier.PUBLIC, Modifier.STATIC),

    BUILD_SERIAL_MAP("buildSerialMap", ClassUtils.getParameterizedStringMap(), Modifier.PUBLIC, Modifier.STATIC);

    private String name;

    private TypeName returns;

    private List<Modifier> modifiers = new ArrayList<>();

    MethodEnum(String name, TypeName returns, Modifier... modifiers) {
        this.name = name;
        this.returns = returns;
        this.modifiers.addAll(Arrays.asList(modifiers));
    }

    public String getName() {
        return name;
    }

    public TypeName getReturns() {
        return returns;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public MethodSpec.Builder getMethodBuilder() {
        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(getName());
        methodBuilder.addModifiers(modifiers);
        methodBuilder.returns(getReturns());
        return methodBuilder;
    }
}
