package org.asdfgamer.utils.config;

/**
 * This is an example for an Enum with Settings.
 *
 * @author ASDFGamer
 */
@SuppressWarnings("unused")
public enum SettingsEnum
{
    test("hello"),
    testNumber(1);

    private final EinstellungenProperty SETTING;

    /**
     * This constructor is for Enums important, because this lets you use one constructor for all cases.
     * The disadvantage of this constructor is that Errors with the parameters aren't found during compiling but during
     * runtime.
     *
     * @param args This are the arguments that get used to create the Setting..
     */
    SettingsEnum(Object... args)
    {

        SETTING = Einstellungen.neueEinstellung(args);
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

    public EinstellungenProperty SETTINGProperty()
    {

        return SETTING;
    }
}
