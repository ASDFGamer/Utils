package org.asdfgamer.utils.config.internal;

import org.asdfgamer.utils.config.SettingsProperty;

import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;

public class SettingsName implements Runnable
{
    private final static Logger LOG = getLogger(SettingsName.class.getName());

    private final SettingsInformation INFO;

    private final SettingsProperty SETTING;

    public SettingsName(SettingsProperty setting, SettingsInformation info)
    {
        this.SETTING = setting;
        this.INFO = info;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run()
    {
        if (!INFO.getClassName().isEmpty())
        {
            try
            {
                setSettingNamesClass(Class.forName(INFO.getClassName()), SETTING);
            } catch (ClassNotFoundException e)
            {
                LOG.warning(bundle.getString("classNotFound") + INFO.getClassName());
            }
        }
    }

    /**
     * This adds the Name of the Setting to the SettingProperty
     *
     * @param settings The Class with the settings.
     */
    private void setSettingNamesClass(Object settings, SettingsProperty property)//TODO recreate this function in SettingsBuilder.
    {

        Map<String, SettingsProperty> stringSettingsPropertyMap = SettingUtils.getFields(settings);
        for (Map.Entry<String, SettingsProperty> entry : stringSettingsPropertyMap.entrySet())
        {
            if (entry.getValue() != null)
            {
                if (entry.getValue().equals(property))
                {
                    INFO.setSettingName(entry.getKey());
                }
            }
        }

    }
}
