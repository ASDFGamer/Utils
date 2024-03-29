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
@SuppressWarnings({"UnusedReturnValue"})
class SettingsBuilder
{

    private final static Logger LOG = getLogger(SettingsBuilder.class.getName());

    private String defaultValue = "";

    private int lineNumber = -1;

    private String className = "";

    private Boolean internalValue = null;

    private boolean changeListener = true;

    private Double minimumValue = null;

    private Double maximumValue = null;

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
    Setting build()
    {

        addClass();
        Setting setting;
        SettingsInformation info = new SettingsInformation(className, lineNumber);
        if (defaultValueList != null)
        {
            setting = new Setting(defaultValueList,internalValue, info);
        } else
        {
            setting = new Setting(defaultValue,internalValue, info);
        }
        if (maximumValue != null)
        {
            if (maximumInt)
            {
                setting.setMaximumValue(maximumValue.intValue());
            } else
            {
                setting.setMaximumValue(maximumValue);
            }
        }
        if (minimumValue != null)
        {
            if (minimumInt)
            {
                setting.setMinimumValue(minimumValue.intValue());
            } else
            {
                setting.setMinimumValue(minimumValue);
            }
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
    SettingsBuilder setDefaultValue(String value)
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
    SettingsBuilder setDefaultValue(String[] value)
    {
        this.defaultValueList = Arrays.asList(value);
        return this;
    }

    /**
     * This sets if this setting is an internal value.
     * By default this is false.
     *
     * @param internal This sets if this setting is an internal value
     * @return The used SettingsBuilder
     */
    SettingsBuilder setInternalValue(boolean internal)
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
    SettingsBuilder addChangeListener(boolean add)
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
    SettingsBuilder setMinimumValue(int minimum)
    {

        this.minimumValue = (double) minimum;
        this.minimumInt = true;
        return this;
    }

    /**
     * This sets the Maximum value for this Setting.
     *
     * @param maximum The maximum value for this Setting.
     * @return The used SettingsBuilder
     */
    SettingsBuilder setMaximumValue(int maximum)
    {

        this.maximumValue = (double) maximum;
        this.maximumInt = true;
        return this;
    }

    /**
     * This sets the Minimum value for this Setting.
     *
     * @param minimum The minimum value for this Setting.
     * @return The used SettingsBuilder
     */
    SettingsBuilder setMinimumValue(double minimum)
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
    SettingsBuilder setMaximumValue(double maximum)
    {

        this.maximumValue = maximum;
        this.maximumInt = false;
        return this;
    }

}
