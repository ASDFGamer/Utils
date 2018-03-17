package org.asdfgamer.utils.config;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.SettingUtils.bundle;

/**
 * This Class holds all Information of an Setting that belongs to the Setting, but isn't related to the value of the Setting.
 * TODO internalize Outputs
 * TODO add JavaDoc
 */
public class SettingsInformation
{
    private final static Logger LOG = getLogger(SettingsInformation.class.getName());
    /**
     * This notes that something changed in some setting. TODO check if some way is implemented to see this for a class.
     */
    private static boolean anySettingChanged = false;

    /**
     * This text shows some information about the setting.
     */
    private final String informationText;

    /**
     * This is the Name of the class in which the setting is created
     */
    private final String className;

    /**
     * This is the LineNumber where the Setting gets created. In Enums this is the Line where the Setting gets declared, in Classes it is the Line where the Setting gets initialised.
     */
    private final int lineNumber;

    /**
     * This is the Name of the Setting.
     */
    private String settingName;

    /**
     * @param informationText
     * @param settingName
     * @param className
     * @param lineNumber
     */
    public SettingsInformation(String informationText, String settingName, String className, int lineNumber)
    {

        this.informationText = informationText;
        this.className = className;
        this.lineNumber = lineNumber;
        this.settingName = settingName;
    }

    void setSettingName(String name)
    {
        if (settingName.isEmpty())
        {
            this.settingName = name;
        } else
        {
            LOG.warning(bundle.getString("NameAlreadySet"));//TODO
        }
    }

    /**
     * @return
     */
    public static boolean isAnySettingChanged()
    {

        return anySettingChanged;
    }

    /**
     *
     */
    public static void setAnySettingChanged()
    {

        SettingsInformation.anySettingChanged = true;
    }

    /**
     * @return
     */
    public String getInformationText()
    {

        return informationText;
    }

    /**
     * @return
     */
    public String getClassName()
    {

        return className;
    }

    /**
     * @return
     */
    public int getLineNumber()
    {

        return lineNumber;
    }

    /**
     * @return
     */
    public String getSettingName()
    {

        return settingName;
    }
}
