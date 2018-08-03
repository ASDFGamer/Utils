package org.asdfgamer.utils.config.internal;

import org.asdfgamer.utils.config.Setting;
import org.asdfgamer.utils.config.annotations.SettingInfo;
import org.asdfgamer.utils.other.Utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class SettingUtils
{

    private final static Logger LOG = getLogger(SettingUtils.class.getName());

    private static Locale locale = Locale.getDefault();

    public final static ResourceBundle bundle = ResourceBundle.getBundle("config/Settings", locale);//TODO test if a change in language works

    public static Map<String, Setting> getFields(Object settings)
    {
        if (Utils.isEnumElement(settings))
        {
            return getFieldsFromEnum(settings);
        } else
        {
            return Utils.getFields(settings);
        }
    }

    private static Map<String, Setting> getFieldsFromEnum(Object object)
    {
        Field[] enumConstants;
        if (object instanceof Class)
        {
            enumConstants = ((Class) object).getFields();
        } else
        {
            enumConstants = object.getClass().getFields();
        }
        Map<String, Setting> settings = new HashMap<>();
        for (Field enumConstant : enumConstants)
        {
            try
            {
                if (enumConstant.get(null) != null)
                    try
                    {
                        Method[] methods = enumConstant.get(null).getClass().getMethods();
                        for (Method method : methods)
                        {
                            if (method.getReturnType().equals(Setting.class))
                            {
                                settings.put(enumConstant.getName(), (Setting) method.invoke(enumConstant.get(null)));
                            }
                        }
                    } catch (IllegalAccessException | InvocationTargetException e)
                    {
                        e.printStackTrace();
                    }
            } catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
        }
        return settings;
    }

    /**
     * This finds all SettingsProperties from the given Enum and returns them as List.
     *
     * @param enumObject The Class-object of the enum
     * @return A List with all Settings
     */
    private static List<Setting> getSettingsFromEnum(Object enumObject)
    {

        if (Utils.isEnumElement(enumObject))
        {
            List<Setting> settings = new LinkedList<>();
            Object[] enumConstants = ((Class) enumObject).getEnumConstants();
            for (Object enumConstant : enumConstants)
            {
                Method[] methods = enumConstant.getClass().getDeclaredMethods();
                for (Method method : methods)
                {
                    if (method.getReturnType().equals(Setting.class))
                    {
                        try
                        {
                            Setting setting = (Setting) method.invoke(enumConstant);
                            //setting.setSettingName(((Enum) enumConstant).name());
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
    public static Map<String, List<Setting>> sortSettingsInClasses(List<Setting> settings)
    {

        Map<String, List<Setting>> settingClasses = new HashMap<>();
        for (Setting setting : settings)
        {
            if (settingClasses.containsKey(setting.getClassName()))
            {
                settingClasses.get(setting.getClassName()).add(setting);
            } else
            {
                List<Setting> className = new LinkedList<>();
                className.add(setting);
                settingClasses.put(setting.getClassName(), className);
            }
        }
        return settingClasses;
    }

    /**
     * This are all values that can be interpreted as 'true'
     * This are the values true and the version in the used language (german:wahr)
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String[] TRUE_VALUES = {"true"};

    public static List<Setting> getSettingsFromObject(Object object)
    {

        if (Utils.isEnumElement(object))
        {
            return getSettingsFromEnum(object);
        } else
        {
            return getSettingsFromClass(object);
        }
    }

    private static List<Setting> getSettingsFromClass(Object object)
    {

        try
        {
            List<Setting> settings = new LinkedList<>();
            Map<String, Setting> stringSettingsPropertyMap = Utils.getFields(object);
            for (Map.Entry<String, Setting> entry : stringSettingsPropertyMap.entrySet())
            {
                settings.add(entry.getValue());
            }
            return settings;
        } catch (Exception e)
        {
            LOG.severe(bundle.getString("cantLoadSetting") + object.getClass().getName());
            return new LinkedList<>();
        }
    }

    /**
     * This are all values that can be interpreted as 'false'
     * This are the values false and the version in the used language (german:falsch)
     */
    @SuppressWarnings("SpellCheckingInspection")
    public static String[] FALSE_VALUES = {"false"};

    /**
     * This is needed, because to initialise the Setting that saves the language this needs to be initialised.
     *
     * @param newLocale The new locale
     */
    public static void setLocale(Locale newLocale)
    {

        TRUE_VALUES = new String[]{TRUE_VALUES[0], bundle.getString("true_value")};
        FALSE_VALUES = new String[]{FALSE_VALUES[0], bundle.getString("false_value")};
        locale = newLocale;
    }

    /**
     * This returns the Annotation for the setting.
     *
     * @param setting The Setting with the annotation.
     * @return The Annotation or null if it doesn't exists.
     * @throws IllegalStateException If the setting wasn't instantiated.
     */
    public static SettingInfo getAnnotation(Setting setting) throws IllegalStateException
    {
        if (!setting.getClassName().isEmpty())
        {
            Field settingsField = getField(setting);
            if (settingsField == null)
            {
                throw new IllegalStateException(bundle.getString("SettingNotInstantiated"));
            } else
            {
                return settingsField.getAnnotation(SettingInfo.class);
            }
        }
        LOG.warning(bundle.getString("classNameIsEmpty"));
        return null;
    }

    /**
     * This returns the Field of the Setting
     *
     * @param setting The Setting
     * @return The Field of the setting, if the Setting is initialized.
     */
    private static Field getField(Setting setting)
    {
        if (setting == null)
        {
            return null;
        }
        try
        {
            if (setting.getSettingName() != null && !setting.getSettingName().isEmpty())
            {
                return Class.forName(setting.getClassName()).getField(setting.getSettingName());
            } else
            {
                return Utils.getStaticField(Class.forName(setting.getClassName()), setting);
            }
        } catch (ClassNotFoundException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
