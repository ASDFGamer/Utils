package org.asdfgamer.utils.config.tests;

import org.asdfgamer.utils.config.Setting;
import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.annotations.SettingInfo;

import java.lang.annotation.ElementType;

/**
 * This Enum gets Used in the tests
 *
 * @author ASDFGamer
 */
@SuppressWarnings("unused")
public enum TestEnum
{
    @SettingInfo(caption = "testen")
    test("test"),
    testInt(1),
    testDouble(1.1),
    testBoolean(true),
    testEnum(ElementType.ANNOTATION_TYPE),
    /*qtest("test"),
    qtestInt(1),
    qtestDouble(1.1),
    qtestBoolean(true),
    wtest("test"),
    wtestInt(1),
    wtestDouble(1.1),
    wtestBoolean(true),
    etest("test"),
    etestInt(1),
    etestDouble(1.1),
    etestBoolean(true),*/;

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
     * continence. The getter for the Property is important because it gets used internally.
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
}
