package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * This creates SettingsProperties und manages SettingsProperties and Setting-classes.
 * <p>
 * <b> Every SettingsProperty has to be public.</b>
 * Otherwise are there problems while saving and loading.
 *
 * @author ASDFGamer
 * @version 0.9
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Settings
{

    /**
     * The used Logger.
     */
    private final static Logger LOG = getLogger(Settings.class.getName());

    /**
     * This shows if {@link Settings#newSetting(Object[])} was used to initiate the construction of the last Setting
     */
    private static boolean objArrayCreator = false;

    /**
     * The name of the program that creates the settings.
     */
    private final String NAME;

    /**
     * The used SettingsStorage.
     */
    private final SettingsStorage STORAGE;

    /**
     * This creates a new Settings-object.
     * The used storage is a properties file in the systems standard configuration folder.
     *
     * @param programName The Name of the program that creates the settings.
     */
    public Settings(String programName)
    {

        this.NAME = programName;
        this.STORAGE = new PropertiesFileStorage(programName);
    }

    /**
     * This creates a new Settings-object.
     *
     * @param programName The Name of the program that creates the settings.
     * @param storage     The Methode that saves the Settings persistent.
     */
    public Settings(String programName, SettingsStorage storage)
    {

        this.NAME = programName;
        this.STORAGE = storage;
    }

    /**
     * This creates a simple SettingsProperty without any predefined value.
     *
     * @return The new Setting
     */
    public static SettingsProperty newSetting()
    {

        SettingsProperty property = new SettingsProperty();
        property.addListener(SettingsListener.getSettingChange(property));
        return property;
    }

    /**
     * This takes an Array of Objects with undefined length and creates a setting from that.
     * This is only a convenience methode and forwards the args to the right methode.
     * This is the preferred methode if it should be used in a constructor of an enum.
     * @param args This is an array of objects which should be analyzed to create the new setting.
     * @return The new Setting
     */
    public static SettingsProperty newSetting(Object[] args)
    {

        objArrayCreator = true;
        switch (args.length)
        {
            case 0:
                return new SettingsProperty();
            case 1:
                return oneArgument(args[0]);
            case 2:
                return twoArguments(args);
            case 3:
                return threeArguments(args);
            default:
                LOG.warning("Es wurden zu viele Argumente für die Einstellung angegeben: " + Arrays.toString(args));
                return newSetting();

        }
    }
//

    /**
     * This transforms an Object to an SettingsProperty with the same Type as the argument.
     *
     * @param setting The argument for the setting as String, Integer, Double or Boolean.
     * @return The new Setting.
     */
    public static SettingsProperty oneArgument(Object setting)
    {


        if (setting instanceof String)
        {
            return newSetting((String) setting);
        } else if (setting instanceof Integer)
        {
            return newSetting((Integer) setting);
        } else if (setting instanceof Double)
        {
            return newSetting((Double) setting);
        } else if (setting instanceof Boolean)
        {
            return newSetting((Boolean) setting);
        }
        LOG.warning("Die Einstellung mit einem Argument ist von einem nicht unterstützten Typ.");
        return newSetting();
    }

    /**
     * This Methode interprets an Array with two Object in such a way that it creates an SettingsProperty.
     *
     * @param args The array with two arguments.
     * @return The new Setting.
     */
    private static SettingsProperty twoArguments(Object[] args)
    {

        if (args.length < 2)
        {
            LOG.warning("Die Methode erwartet Array mit zwei Argumenten, diese hat aber weniger.");
            return newSetting();
        }

        if (args[0] instanceof String && args[1] instanceof Boolean)
        {
            return newSetting((String) args[0], (Boolean) args[1]);
        } else if (args[0] instanceof Integer && args[1] instanceof Boolean)
        {
            return newSetting((Integer) args[0], (Boolean) args[1]);
        } else if (args[0] instanceof Double && args[1] instanceof Boolean)
        {
            return newSetting((Double) args[0], (Boolean) args[1]);
        } else if (args[0] instanceof Boolean && args[1] instanceof Boolean)
        {
            return newSetting((Boolean) args[0], (Boolean) args[1]);
        }
        LOG.warning("Die Einstellung mit zwei Argumenten ist von einem nicht unterstützten Typ.");
        return newSetting();
    }
//

    /**
     * This Methode interprets an Array with three Object in such a way that it creates an SettingsProperty.
     *
     * @param settings The array with three arguments.
     * @return The new Setting.
     */
    private static SettingsProperty threeArguments(Object[] settings)
    {

        if (settings.length < 3)
        {
            LOG.warning("Die Methode erwartet Array mit drei Argumenten, diese hat aber weniger.");
            return newSetting();
        }

        if (settings[0] instanceof Integer && settings[1] instanceof Integer && settings[2] instanceof Integer)
        {
            return newSetting((Integer) settings[0], (Integer) settings[1], (Integer) settings[2]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Double && settings[2] instanceof Double)
        {
            return newSetting((Double) settings[0], (Double) settings[1], (Double) settings[2]);
        }
        LOG.warning("Die Einstellung mit drei Argumenten ist von einem nicht unterstützten Typ.");
        return newSetting();
    }

    /**
     * This creates an SettingsProperty with a String as default value.
     *
     * @param defaultValue The default value
     * @return The new Setting
     */
    public static SettingsProperty newSetting(String defaultValue)
    {

        SettingsProperty property = new SettingsProperty(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);

        return property;
    }

    /**
     * This creates an SettingsProperty with a Integer as default value.
     *
     * @param defaultValue The default value
     * @return The new Setting.
     */
    public static SettingsProperty newSetting(int defaultValue)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Double as default value.
     *
     * @param defaultValue The default value
     * @return The new Setting
     */
    public static SettingsProperty newSetting(double defaultValue)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setDouble(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a boolean as default value.
     *
     * @param defaultValue The default value.
     * @return The new Setting
     */
    public static SettingsProperty newSetting(boolean defaultValue)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setBoolean(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);
        return property;
    }

    //Interne Einstelungen

    /**
     * This creates an SettingsProperty with a String as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @return The new setting
     */
    public static SettingsProperty newSetting(String defaultValue, boolean internal)
    {

        SettingsProperty property = new SettingsProperty(defaultValue, internal);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Integer as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @return The new setting
     */
    public static SettingsProperty newSetting(int defaultValue, boolean internal)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Double as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @return The new setting
     */
    public static SettingsProperty newSetting(double defaultValue, boolean internal)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        property.setDouble(defaultValue);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a boolean as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @return The new setting
     */
    public static SettingsProperty newSetting(boolean defaultValue, boolean internal)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        property.setBoolean(defaultValue);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum  The lowest allowed value.
     * @param maximum  The highest allowed value.
     * @return The new setting
     */
    public static SettingsProperty newSetting(int defaultValue, int minimum, int maximum)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setMinimumValue(minimum);
        property.setMaximumValue(maximum);
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum  The lowest allowed value.
     * @param maximum  The highest allowed value.
     * @return The new setting
     */
    public static SettingsProperty newSetting(double defaultValue, double minimum, double maximum)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setMinimumValue(minimum);
        property.setMaximumValue(maximum);
        property.addListener(SettingsListener.getSettingChange(property));
        addClass(property);
        return property;
    }

    /**
     * This adds the Class in wich the setting ist declared to the list of all classes with settings.
     * @param property The Setting.
     */
    private static void addClass(SettingsProperty property)
    {

        String className;
        if (objArrayCreator)
        {
            className = Thread.currentThread().getStackTrace()[5].getClassName();
            objArrayCreator = false;
        } else
        {
            className = Thread.currentThread().getStackTrace()[3].getClassName();
        }
        property.setClassName(className);
        SettingClassInfo.add(className);
    }

    /**
     * This Methode saves the Settings from the given Class.
     *
     * @param settings This ist the Class with the settings.
     * @return true, if saving was successful, otherwise false.
     */
    public boolean save(Object settings)
    {

        if (isEnum(settings))
        {
            return STORAGE.save(getSettingsFromEnum(settings));
        }
        return STORAGE.save(Utils.getFields(settings));
    }

    /**
     * This tests if the given object is the Class-object of an enum.
     *
     * @param enumObject The Object that should be checked.
     * @return true, if it ist an enum, otherwise false.
     */
    private boolean isEnum(Object enumObject)
    {

        return (enumObject instanceof Class) && (((Class) enumObject).isEnum());
    }

    /**
     * This finds all SettingsProperties from the given Enum and returns them as Map.
     *
     * @param enumObject The Class-object of the enum
     * @return A Map with all Settings
     */
    private Map<String, SettingsProperty> getSettingsFromEnum(Object enumObject)
    {
        if (isEnum(enumObject))
        {
            return getSettingsFromEnum(((Class) enumObject).getName());
        }
        return null;
    }

    /**
     * This Methode saves all Settings to the given Storage.
     *
     * @return true, if all settings could be saved, otherwise false.
     */
    public boolean save()
    {

        Set<String> classes = SettingClassInfo.getClasses();
        boolean result = true;
        for (String classObj : classes)
        {
            try
            {
                if (isEnum(Class.forName(classObj)))
                {
                    result = STORAGE.save(getSettingsFromEnum(classObj));
                } else
                {
                    result = STORAGE.save(Utils.getFields(Class.forName(classObj))) && result;
                }
            } catch (ClassNotFoundException e)
            {
                LOG.warning("Bei der Klasse " + classObj + "gab es ein Problem beim Wiederfinden der " +
                        "Klasse zum Speichern. Somit wird sie nun nicht gespeichert.");
                result = false;
            }

        }
        return result;
    }

    /**
     * This finds all SettingsProperties from the given Enum and returns them as Map.
     *
     * @param enumName The name of the enum
     * @return A Map with all Settings
     */
    private Map<String, SettingsProperty> getSettingsFromEnum(String enumName)
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
                            LOG.warning("Es gab ein Problem beim abrufen einer Einstellung aus der Funktion: " + method.getName() + " \n Dies tritt z.B. auf, falls argumente gefordert sind.");
                        }
                    }
                }

            }
        } catch (ClassNotFoundException e)
        {
            LOG.severe("Es gab ein Problem beim Wiederfinden der Klasse \"" + enumName + "\".");
            e.printStackTrace();
        }
        return settings;
    }

    /**
     * This loads all values for the Settings in the given Class.
     *
     * @param settings The Class with the settings.
     * @return true, if the settings got loaded successfully, otherwise false.
     */
    public boolean load(Object settings)
    {

        if (isEnum(settings))
        {
            return STORAGE.load(getSettingsFromEnum(settings));
        }
        return STORAGE.load(Utils.getFields(settings));
    }

    /**
     * This loads the Setting for all Classes.
     * If you want to check if one specific class got loaded successfully you have to use {@link SettingClassInfo}
     * after this.
     *
     * @return true, if all settings loaded successful, otherwise false.
     */
    public boolean load()
    {

        Set<String> classes = SettingClassInfo.getClasses();

        boolean result = true;
        for (String className : classes)
        {
            try
            {
                if (isEnum(Class.forName(className)))
                {
                    result = STORAGE.load(getSettingsFromEnum(className)) && result;
                } else
                {
                    result = STORAGE.load(Utils.getFields(Class.forName(className))) && result;
                }
            } catch (ClassNotFoundException e)
            {
                LOG.warning("Bei der Klasse " + className + "gab es ein Problem beim Wiederfinden der " +
                        "Klasse zum Speichern. Somit wird sie nun nicht gespeichert.");
                result = false;
            }
        }
        return result;
    }
}
