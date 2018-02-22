package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class SettingUtils
{

    private final static Logger LOG = getLogger(SettingUtils.class.getName());

    private static Locale locale = Locale.getDefault();

    public final static ResourceBundle bundle = ResourceBundle.getBundle("config/Settings", locale);//TODO test if a change in language works

    /**
     * This finds all SettingsProperties from the given Enum and returns them as List.
     *
     * @param enumObject The Class-object of the enum
     * @return A List with all Settings
     */
    private static List<SettingsProperty> getSettingsFromEnum(Object enumObject)
    {

        if (Utils.isEnum(enumObject))
        {
            List<SettingsProperty> settings = new LinkedList<>();
            Object[] enumConstants = ((Class) enumObject).getEnumConstants();
            for (Object enumConstant : enumConstants)
            {
                Method[] methods = enumConstant.getClass().getDeclaredMethods();
                for (Method method : methods)
                {
                    if (method.getReturnType().equals(SettingsProperty.class))
                    {
                        try
                        {
                            SettingsProperty setting = (SettingsProperty) method.invoke(enumConstant);
                            setting.setSettingName(((Enum) enumConstant).name());
                            if (!settings.contains(setting))
                            {
                                settings.add(setting);
                            }
                        } catch (ReflectiveOperationException e)
                        {
                            LOG.warning(bundle.getString("problemWithFunctionCall") + method.getName() + " \n" + bundle.getString("problemWithFunctionCallInfo"));
                        }
                    }
                }

            }

            return settings;
        }
        return null;
    }

    /**
     * This creates a Map which has in the Classname as Key and as value a List with all Settings of that Class.
     *
     * @param settings This are all settings that should be sorted as List.
     *                 The settings can be from different classes but don't have to be.
     * @return A Map which has in the Classname as Key and as value a List with all Settings of that Class.
     */
    @SuppressWarnings("WeakerAccess")
    public static Map<String, List<SettingsProperty>> sortSettingsInClasses(List<SettingsProperty> settings)
    {

        Map<String, List<SettingsProperty>> settingClasses = new HashMap<>();
        for (SettingsProperty setting : settings)
        {
            if (settingClasses.containsKey(setting.getClassName()))
            {
                settingClasses.get(setting.getClassName()).add(setting);
            } else
            {
                List<SettingsProperty> className = new LinkedList<>();
                className.add(setting);
                settingClasses.put(setting.getClassName(), className);
            }
        }
        return settingClasses;
    }

    /**
     * This is needed, because to initialise the Setting that saves the language this needs to be initialised.
     *
     * @param newLocale The new locale
     */
    protected static void setLocale(Locale newLocale)
    {

        locale = newLocale;
    }

    public static List<SettingsProperty> getSettingsFromObject(Object object)
    {

        if (Utils.isEnum(object))
        {
            return getSettingsFromEnum(object);
        } else
        {
            return getSettingsFromClass(object);
        }
    }

    private static List<SettingsProperty> getSettingsFromClass(Object object)
    {

        try
        {
            List<SettingsProperty> settings = new LinkedList<>();
            Map<String, SettingsProperty> stringSettingsPropertyMap = Utils.getFields(object);
            for (Map.Entry<String, SettingsProperty> entry : stringSettingsPropertyMap.entrySet())
            {
                entry.getValue().setSettingName(entry.getKey());
                settings.add(entry.getValue());
            }
            return settings;
        } catch (Exception e)
        {
            LOG.severe(bundle.getString("cantLoadSetting") + object.getClass().getName());
            return new LinkedList<>();
        }
    }
}
