package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.SettingUtils.bundle;

public class SettingsBuilder
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

    /**
     * This adds the Class in which the setting ist declared to the list of all classes with settings.
     */
    private void addClass()
    {
        //LOG.warning("2" + Thread.currentThread().getStackTrace()[2].getClassName());
        //LOG.warning("3" + Thread.currentThread().getStackTrace()[3].getClassName());
        //LOG.warning("4" + Thread.currentThread().getStackTrace()[4].getClassName());
        //LOG.warning("5" + Thread.currentThread().getStackTrace()[5].getClassName());
        if (Thread.currentThread().getStackTrace()[4].getClassName().equals(Settings.class.getName()))
        {

            className = Thread.currentThread().getStackTrace()[5].getClassName();//TODO update the Numbers
            LOG.warning("1" + className);
            setLine(Thread.currentThread().getStackTrace()[5]);
        } else
        {
            className = Thread.currentThread().getStackTrace()[4].getClassName();
            LOG.warning("2" + className);
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
     * This adds the Name of the Setting to the SettingProperty
     *
     * @param settings The Class with the settings.
     */
    private void setSettingNamesClass(Object settings, SettingsProperty property)//TODO recreate this function in SettingsBuilder.
    {

        Map<String, SettingsProperty> stringSettingsPropertyMap = Utils.getFields(settings);
        for (Map.Entry<String, SettingsProperty> entry : stringSettingsPropertyMap.entrySet())
        {
            //LOG.warning("SettingsBuilder.setSettingNamesClass Zeile 91");
            if (entry.getValue() != null && entry.getValue().equals(property))
            {
                LOG.warning("settingsname for" + property.toString() + " = " + entry.getKey());
                settingName = entry.getKey();
            }
        }

    }

    public SettingsProperty build()
    {

        addClass();

        SettingsProperty setting = new SettingsProperty(defaultValue, internalValue);
        setting.setMaximumValue(maximumValue);
        setting.setMinimumValue(minimumValue);
        if (changeListener)
        {
            setting.addListener(SettingsListener.getSettingChange(setting));
        }
        if (!className.isEmpty())
        {
            try
            {
                setSettingNamesClass(Class.forName(className), setting);
            } catch (ClassNotFoundException e)
            {
                LOG.warning(bundle.getString("classNotFound") + className);
            }
        }
        SettingsInformation info = new SettingsInformation(informationText, settingName, className, lineNumber);
        setting.setSettingsInformation(info);
        return setting;
    }

    public SettingsBuilder setDefaultValue(String value)
    {

        this.defaultValue = value;
        return this;
    }

    public SettingsBuilder setName(String name)
    {

        this.settingName = name;
        return this;
    }


    public SettingsBuilder setInformationText(String info)
    {

        this.informationText = info;
        return this;
    }

    public SettingsBuilder setInternalValue(boolean internal)
    {

        this.internalValue = internal;
        return this;
    }

    public SettingsBuilder addChangeListener(boolean add)
    {

        this.changeListener = add;
        return this;
    }

    public SettingsBuilder setMinimumValue(double minimum)
    {

        this.minimumValue = minimum;
        return this;
    }

    public SettingsBuilder setMaximumValue(double maximum)
    {

        this.maximumValue = maximum;
        return this;
    }
}
