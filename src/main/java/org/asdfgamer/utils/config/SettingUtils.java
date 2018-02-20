package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class SettingUtils
{

    private final static Logger LOG = getLogger(SettingUtils.class.getName());

    private static Locale locale = Locale.getDefault();

    public final static ResourceBundle bundle = ResourceBundle.getBundle("config/Settings", locale);//TODO test if a change in language works

    /**
     * This finds all SettingsProperties from the given Enum and returns them as Map.
     *
     * @param enumObject The Class-object of the enum
     * @return A Map with all Settings
     */
    public static Map<String, SettingsProperty> getSettingsFromEnum(Object enumObject)
    {

        if (Utils.isEnum(enumObject))
        {
            return getSettingsFromEnum(((Class) enumObject).getName());
        }
        return null;
    }

    /**
     * This finds all SettingsProperties from the given Enum and returns them as Map.
     *
     * @param enumName The name of the enum
     * @return A Map with all Settings
     */
    public static Map<String, SettingsProperty> getSettingsFromEnum(String enumName)
    {

        Map<String, SettingsProperty> settings = new HashMap<>();
        try
        {
            Object[] enumConstants = Class.forName(enumName).getEnumConstants();
            for (Object enumConstant : enumConstants)
            {
                Method[] methods = enumConstant.getClass().getDeclaredMethods();
                for (Method method : methods)
                {
                    if (method.getReturnType().equals(SettingsProperty.class))
                    {
                        try
                        {
                            settings.putIfAbsent(((Enum) enumConstant).name(), (SettingsProperty) method.invoke(enumConstant));
                        } catch (ReflectiveOperationException e)
                        {
                            LOG.warning(bundle.getString("problemWithFunctionCall") + method.getName() + " \n" + bundle.getString("problemWithFunctionCallInfo"));
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e)
        {
            LOG.severe(bundle.getString("classNotFound") + enumName + "'.");
            e.printStackTrace();
        }
        return settings;
    }

    /**
     * This creates a nested Map which has in the first map the Classname and in the second Map the name of the setting
     * an the setting itself.
     *
     * @param settings This are all settings that should be sorted as Map. The Key is the Name of the setting and the
     *                 value is the setting itself. The settings can be from different classes but don't have to be.
     * @return A nested Map which has in the first map the Classname and in the second Map the name of the setting an
     * the setting itself.
     */
    @SuppressWarnings("WeakerAccess")
    public static Map<String, Map<String, SettingsProperty>> sortSettingsInClasses(Map<String, SettingsProperty> settings)
    {

        Map<String, Map<String, SettingsProperty>> settingClasses = new HashMap<>();
        for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
        {
            if (settingClasses.containsKey(setting.getValue().getClassName()))
            {
                settingClasses.get(setting.getValue().getClassName()).put(setting.getKey(), setting.getValue());
            } else
            {
                Map<String, SettingsProperty> className = new HashMap<>();
                className.put(setting.getKey(), setting.getValue());
                settingClasses.put(setting.getValue().getClassName(), className);
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
}
