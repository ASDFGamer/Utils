package org.asdfgamer.utils.config;

import java.util.Locale;

/**
 * This are all Settings that are needed from the Configuration-management.
 *
 * This is also an example for an Enum with Settings.
 *
 * @author ASDFGamer
 */
@SuppressWarnings("unused")
@Caption("Config for Settings")
//@NoGUI
public enum SettingsConfig
{
    @Caption("Allgemein")
    language(Locale.getDefault().toLanguageTag()),
    test("Hallo"),
    du("auch"),
    @Caption("Spezifisch")
    geanu("du");

    private final SettingsProperty SETTING;

    /**
     * This constructor is for Enums important, because this lets you use one constructor for all cases.
     * The disadvantage of this constructor is that Errors with the parameters aren't found during compiling but during
     * runtime.
     *
     * @param args This are the arguments that get used to create the Setting..
     */
    SettingsConfig(Object... args)
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

    public SettingsProperty SETTINGProperty()
    {

        return SETTING;
    }
}
