package org.asdfgamer.utils.config;

import org.asdfgamer.utils.config.internal.SettingsInformation;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;

/**
 * This builder is used to create settings and add all necessary values.
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
class SettingsBuilder
{

    private final static Logger LOG = getLogger(SettingsBuilder.class.getName());

    private String defaultValue = "";

    private int lineNumber = -1;

    private String settingName = "";

    private String className = "";

    private String informationText = "";

    private boolean internalValue = false;

    private boolean changeListener = true;

    private double minimumValue;

    private double maximumValue;

    private boolean maximumInt;

    private boolean minimumInt;

    private List<String> defaultValueList = null;

    /**
     * This builder can only be used by classes from this package.
     */
    SettingsBuilder()
    {
    }

    /**
     * This builds an Setting form the given values.
     *
     * @return A new Setting
     */
    public Setting build()
    {

        addClass();
        Setting setting;
        SettingsInformation info = new SettingsInformation(className, lineNumber);
        if (defaultValueList != null)
        {
            setting = new Setting(defaultValueList, internalValue, info);
        } else
        {
            setting = new Setting(defaultValue, internalValue, info);
        }
        if (maximumInt)
        {
            setting.setMaximumValue((int) maximumValue);
        } else
        {
            setting.setMaximumValue(maximumValue);
        }
        if (minimumInt)
        {
            setting.setMinimumValue((int) minimumValue);
        } else
        {
            setting.setMinimumValue(minimumValue);
        }

        if (changeListener)
        {
            setting.addListener(SettingsListener.getSettingChange(setting));
        }

        setting.setSettingsInformation(info);
        return setting;
    }

    /**
     * This adds the Class in which the setting ist declared to the list of all classes with settings.
     */
    private void addClass()
    {
        if (Thread.currentThread().getStackTrace()[4].getClassName().equals(Settings.class.getName()))
        {

            className = Thread.currentThread().getStackTrace()[5].getClassName();
            setLine(Thread.currentThread().getStackTrace()[5]);
        } else
        {
            className = Thread.currentThread().getStackTrace()[4].getClassName();
            setLine(Thread.currentThread().getStackTrace()[4]);
        }

        SettingClassInfo.add(className);
    }

    /**
     * This gets the Line number of the property if it is can (the class is an enum) and saves it in the property.
     *
     * @param element The Element from the stacktrace.
     */
    private void setLine(StackTraceElement element)
    {

        try
        {
            if (Class.forName(element.getClassName()).isEnum())//Test if this can work with normal classes. (It cant find the declaration only the initialisation).
            {
                this.lineNumber = element.getLineNumber();
            }
        } catch (ClassNotFoundException e)
        {
            LOG.fine(bundle.getString("classNotFound") + element.getClassName() + "'.");
        }
    }

    /**
     * This sets the default value
     *
     * @param value The default value of the Setting
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setDefaultValue(String value)
    {

        this.defaultValue = value;
        return this;
    }

    /**
     * This sets the default values
     *
     * @param value The default values of the Setting
     *
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setDefaultValue(String[] value)
    {
        this.defaultValueList = Arrays.asList(value);
        return this;
    }

    /**
     * This sets the Name of the Setting
     *
     * @param name The name of the Setting
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setName(String name)
    {

        this.settingName = name;
        return this;
    }

    /**
     * This sets the information text of the Setting
     *
     * @param info The information text of the Setting
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setInformationText(String info)
    {

        this.informationText = info;
        return this;
    }

    /**
     * This sets if this setting is an internal value.
     * By default this is false.
     *
     * @param internal This sets if this setting is an internal value
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setInternalValue(boolean internal)
    {

        this.internalValue = internal;
        return this;
    }

    /**
     * This sets if this setting should have the ChangeListener.
     * By default this is true.
     *
     * @param add This sets if this setting should haven the ChangeListener.
     * @return The used SettingsBuilder
     */
    public SettingsBuilder addChangeListener(boolean add)
    {

        this.changeListener = add;
        return this;
    }

    /**
     * This sets the Minimum value for this Setting.
     *
     * @param minimum The minimum value for this Setting.
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setMinimumValue(int minimum)
    {

        this.minimumValue = minimum;
        this.minimumInt = true;
        return this;
    }

    /**
     * This sets the Maximum value for this Setting.
     *
     * @param maximum The maximum value for this Setting.
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setMaximumValue(int maximum)
    {

        this.maximumValue = maximum;
        this.maximumInt = true;
        return this;
    }

    /**
     * This sets the Minimum value for this Setting.
     *
     * @param minimum The minimum value for this Setting.
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setMinimumValue(double minimum)
    {

        this.minimumValue = minimum;
        this.minimumInt = false;
        return this;
    }

    /**
     * This sets the maximum value for this Setting.
     *
     * @param maximum The maximum value for this Setting.
     * @return The used SettingsBuilder
     */
    public SettingsBuilder setMaximumValue(double maximum)
    {

        this.maximumValue = maximum;
        this.maximumInt = false;
        return this;
    }

}
