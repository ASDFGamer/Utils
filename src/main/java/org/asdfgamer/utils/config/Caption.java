package org.asdfgamer.utils.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This is an annotation that declares an Caption and if the Class with the Caption is saved to an destination with the
 * support to read this captions the caption will be added in the result.
 * This is only possible for the order of the Fields. If you declare the settings in a Class then the settings will be
 * in the order you defined them in the methode and the Captions are before the Element you declared them.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Caption
{

    /**
     * This is the Name of the Caption
     *
     * @return The Name of the Caption
     */
    String value();

}
