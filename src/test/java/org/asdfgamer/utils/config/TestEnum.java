package org.asdfgamer.utils.config;

import org.asdfgamer.utils.config.annotations.Caption;
import org.asdfgamer.utils.config.annotations.SettingInfo;

import java.lang.annotation.ElementType;

/**
 * This Enum gets used in the tests
 *
 * @author ASDFGamer
 */
public enum TestEnum
{
    @Caption("Testwerte")//TODO add a way to get the caption of a Setting (in a later version)
    testString("test"),
    testInt(1),
    testDouble(1.1),
    testBoolean(true),
    testEnum(ElementType.ANNOTATION_TYPE),
    testListString("Dies","ist","ein","test"),
    testListInt(1,2,3),
    testListDouble(1.1,2.2,3.3),
    testListBoolean(true,false),
    testListEnum(testString,testInt,testDouble),
    @SettingInfo(info = "This is a simple Info.", internalValue = true, maximumValue = 6, minimumValue = -5)
    testIntAnnotation(2),
    ;

    private final Setting SETTING;

    /**
     * This constructor is for Enums important, because this lets you use one constructor for all cases.
     * The disadvantage of this constructor is that Errors with the parameters aren't found during compiling but during
     * runtime.
     *
     * @param args This are the arguments that get used to create the Setting..
     */
    TestEnum(Object... args)
    {

        SETTING = Settings.newSetting(args);
    }

    /*
     * Theoretic is of the following methods only the getter for the Property important, but the other are here for
     * continence. The getter for the Property is important because it gets used internally (But ist can be named different).
     */
    public String getSETTING()
    {

        return SETTING.get();
    }

    public void setSETTING(String SETTING)
    {

        this.SETTING.set(SETTING);
    }

    public Setting SETTINGProperty()
    {

        return SETTING;
    }

    /**
     * Only because it is shorter
     */
    public Setting get()
    {

        return SETTINGProperty();
    }
}
