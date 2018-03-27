package org.asdfgamer.utils.config.internal;

import org.asdfgamer.utils.config.Setting;
import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.annotations.SettingInfo;

import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;

/**
 * This Class holds all Information of an Setting that belongs to the Setting, but isn't related to the value of the
 * Setting.
 */
@SuppressWarnings("WeakerAccess")
public class SettingsInformation
{

    private final static Logger LOG = getLogger(SettingsInformation.class.getName());

    /**
     * This notes that something changed in some setting.
     */
    private static boolean anySettingChanged = false;

    /**
     * This text shows some information about the setting.
     */
    private String informationText = null;

    /**
     * This is the Name of the class in which the setting is created
     */
    private final String className;

    /**
     * This is the LineNumber where the Setting gets created. In Enums this is the Line where the Setting gets declared,
     * in Classes it is the Line where the Setting gets initialised.
     */
    private final int lineNumber;

    private Setting setting;

    /**
     * This is the Name of the Setting.
     */
    private String settingName;

    /**
     * This creates an new Information Object for a Setting.
     *
     * @param className  This is the Name of the Class of the Setting.
     * @param lineNumber This is the Line number of the Setting.
    //* @param setting    This is the Setting where this Info-Object gets added.
     */
    public SettingsInformation(String className, int lineNumber)
    {
        this.className = className;
        this.lineNumber = lineNumber;
        //this.setting = setting;
    }

    /**
     * This returns if any Setting is changed.
     *
     * @return If any Setting is changed.
     */
    public static boolean isAnySettingChanged()
    {

        return anySettingChanged;
    }

    /**
     * This gets called if a setting was changed.
     */
    public static void setAnySettingChanged()
    {

        SettingsInformation.anySettingChanged = true;
    }

    /**
     * This returns the Information Text for the Setting. If it can be localized with the ResourceBundle given to
     * {@link Settings} it returns the localized version.
     *
     * @return The Information Text for the Setting. If it can be localized with the ResourceBundle given to
     *         {@link Settings} it returns the localized version.
     */
    public String getInformationText()
    {
        if (informationText == null)
        {
            setInformationText();
        }
        if (Settings.getResourceBundle() != null && Settings.getResourceBundle().containsKey(informationText))
        {
            return Settings.getResourceBundle().getString(informationText);
        }
        return informationText;
    }

    private void setInformationText()
    {
        SettingInfo info = SettingUtils.getAnnotation(setting);
        if (info != null)
        {
            informationText = info.info();
        }
    }

    /**
     * This returns the Class Name for the Setting. If it can be localized with the ResourceBundle given to
     * {@link Settings} it returns the localized version.
     *
     * @return The Class Name for the Setting. If it can be localized with the ResourceBundle given to
     *         {@link Settings} it returns the localized version.
     */
    public String getClassName()
    {

        if (Settings.getResourceBundle() != null && Settings.getResourceBundle().containsKey(className))
        {
            return Settings.getResourceBundle().getString(className);
        }
        return className;
    }

    /**
     * This returns the LineNumber of the setting. If this can't be determined, then this returns -1.
     *
     * @return The LineNumber of the setting. If this can't be determined, then this returns -1.
     */
    public int getLineNumber()
    {

        return lineNumber;
    }

    /**
     * This returns the Name for the Setting. If it can be localized with the ResourceBundle given to
     * {@link Settings} it returns the localized version.
     *
     * @return The Name for the Setting. If it can be localized with the ResourceBundle given to
     *         {@link Settings} it returns the localized version.
     */
    public String getSettingName()
    {

        if (settingName == null || settingName.isEmpty())
        {
            setSettingsName();
        }
        if (Settings.getResourceBundle() != null && Settings.getResourceBundle().containsKey(settingName))
        {
            return Settings.getResourceBundle().getString(settingName);
        }
        return settingName;
    }


    /**
     * This sets the name of the Setting, if no name already exists
     *
     * @param name This is the name of the Setting.
     *
     * @return true, if the name could be set, otherwise false.
     */
    private boolean setSettingName(String name)
    {

        if (settingName == null || settingName.isEmpty())
        {
            this.settingName = name;
            return true;
        } else
        {
            LOG.warning(bundle.getString("nameAlreadySet"));
            return false;
        }
    }

    /**
     * This searches for the name of the Setting and sets it.
     *
     * @return true, if everything was successful, otherwise false.
     */
    private boolean setSettingsName()
    {

        if (!getClassName().isEmpty())
        {
            try
            {
                return setSettingNamesClass(Class.forName(getClassName()), setting);
            } catch (ClassNotFoundException e)
            {
                LOG.warning(bundle.getString("classNotFound") + getClassName());
                return false;
            }
        }
        LOG.warning(bundle.getString("classNameIsEmpty"));
        return false;
    }

    /**
     * This adds the Name of the Setting to the SettingProperty
     *
     * @param settings The Class with the settings.
     *
     * @return true, if it was successful, otherwise false.
     */
    private boolean setSettingNamesClass(Object settings, Setting property)
    {

        Map<String, Setting> stringSettingsPropertyMap = SettingUtils.getFields(settings);
        for (Map.Entry<String, Setting> entry : stringSettingsPropertyMap.entrySet())
        {
            if (entry.getValue() != null && entry.getValue().equals(property))
            {
                return setSettingName(entry.getKey());
            }
        }
        return false;
    }

    public void setSettings(Setting setting)
    {
        this.setting = setting;
    }
}
