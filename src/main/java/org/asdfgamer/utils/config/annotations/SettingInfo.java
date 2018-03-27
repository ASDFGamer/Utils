package org.asdfgamer.utils.config.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface SettingInfo
{

    String info() default "";

    boolean internalValue() default false;

    double minimumValue() default Double.MIN_VALUE;

    double maximumValue() default Double.MAX_VALUE;

    String caption();

    boolean noGUI() default false;
}
