package org.asdfgamer.utils.config.sort;

import org.asdfgamer.utils.config.SettingsProperty;

import static org.asdfgamer.utils.config.SettingUtils.bundle;

/**
 * This is an {@link ListElement} for Settings.
 */
public class SettingElement extends ListElement
{

    /**
     * The Setting of the Element.
     */
    private final SettingsProperty SETTING;

    /**
     * This is the Name of the Setting.
     */
    private final String NAME;

    /**
     * This creates an new SettingElement
     *
     * @param setting The Setting itself
     */
    SettingElement(SettingsProperty setting)
    {

        super(setting.getLineNumber(), "#" + setting.getInformationText() + "(" + bundle.getString("std_value") + " = " + setting.getDefaultValue() + ")" + "\n" + setting.getSettingName() + "=" + setting.get() + "\n");
        this.SETTING = setting;
        this.NAME = setting.getSettingName();
    }

    /**
     * This returns the Name.
     *
     * @return The Name
     */
    public String getName()
    {

        return NAME;
    }

    /**
     * This returns the Setting.
     *
     * @return The Setting.
     */
    public SettingsProperty getSetting()
    {

        return SETTING;
    }
}
