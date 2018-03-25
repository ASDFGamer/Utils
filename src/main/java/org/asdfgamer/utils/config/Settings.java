package org.asdfgamer.utils.config;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.SettingsConfig.language;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;
import static org.asdfgamer.utils.config.internal.SettingUtils.getSettingsFromObject;

/**
 * This creates SettingsProperties und manages SettingsProperties and Setting-classes.
 * <p>
 * <b> Every Setting has to be public.</b>
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

    private static ResourceBundle RESOURCE_BUNDLE;

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

        RESOURCE_BUNDLE = null;
        this.NAME = programName;
        this.STORAGE = new PropertiesFileStorage(programName);
        loadOwnSettings();
    }

    /**
     * This creates a new Settings-object.
     * The used storage is a properties file in the systems standard configuration folder.
     *
     * @param programName    The Name of the program that creates the settings.
     * @param resourceBundle This is an ResourceBundle which contains translations for all Setting names, Class names or
     *                       Information texts.
     */
    public Settings(String programName, ResourceBundle resourceBundle)
    {

        RESOURCE_BUNDLE = resourceBundle;
        this.NAME = programName;
        this.STORAGE = new PropertiesFileStorage(programName);
        loadOwnSettings();
    }

    /**
     * This creates a new Settings-object.
     *
     * @param programName The Name of the program that creates the settings.
     * @param storage     The Methode that saves the Settings persistent.
     */
    public Settings(String programName, SettingsStorage storage)
    {

        RESOURCE_BUNDLE = null;
        this.NAME = programName;
        this.STORAGE = storage;
        loadOwnSettings();
    }

    /**
     * This creates a new Settings-object.
     *
     * @param programName    The Name of the program that creates the settings.
     * @param storage        The Methode that saves the Settings persistent.
     * @param resourceBundle This is an ResourceBundle which contains translations for all Setting names, Class names or
     *                       Information texts.
     */
    public Settings(String programName, SettingsStorage storage, ResourceBundle resourceBundle)
    {

        RESOURCE_BUNDLE = resourceBundle;
        this.NAME = programName;
        this.STORAGE = storage;
        loadOwnSettings();
    }

    // ALL Options for parameter

    /**
     * This creates a simple Setting without any predefined value.
     *
     * @return The new Setting
     */
    public static Setting newSetting()
    {

        return new SettingsBuilder().build();
    }

    /**
     * This takes an Array of Objects with undefined length and creates a setting from that.
     * This is only a convenience methode and forwards the args to the right methode.
     * This is the preferred methode if it should be used in a constructor of an enum.
     *
     * @param args This is an array of objects which should be analyzed to create the new setting.
     *
     * @return The new Setting
     */
    public static Setting newSetting(Object[] args)
    {
        switch (args.length)
        {
            case 0:
                return new SettingsBuilder().build();
            case 1:
                return firstArgument(args[0]).build();
            case 2:
                return twoArguments(args).build();
            case 3:
                return threeArguments(args).build();
            case 4:
                return fourArguments(args).build();
            case 5:
                return fiveArguments(args).build();
            default:
                LOG.warning(bundle.getString("toManyArgs") + Arrays.toString(args));
                return new SettingsBuilder().build();
        }
    }

    //STRINGS

    /**
     * This creates an Setting with a String as default value.
     *
     * @param defaultValue The default value
     *
     * @return The new Setting
     */
    public static Setting newSetting(String defaultValue)
    {

        return newSetting(new Object[]{defaultValue});
    }

    /**
     * This creates an Setting with a String as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(String defaultValue, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, internal});
    }

    /**
     * This creates an Setting with a String as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     *
     * @return The new Setting
     */
    public static Setting newSetting(String defaultValue, String description)
    {

        return newSetting(new Object[]{defaultValue, description});
    }

    /**
     * This creates an Setting with a String as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(String defaultValue, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, internal, description});
    }

    //INT

    /**
     * This creates an Setting with a Integer as default value.
     *
     * @param defaultValue The default value
     *
     * @return The new Setting.
     */
    public static Setting newSetting(int defaultValue)
    {

        return newSetting(new Object[]{defaultValue});
    }

    /**
     * This creates an Setting with a Integer as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, internal});
    }

    /**
     * This creates an Setting with a Integer as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     *
     * @return The new Setting.
     */
    public static Setting newSetting(int defaultValue, String description)
    {

        return newSetting(new Object[]{defaultValue, description});
    }

    /**
     * This creates an Setting with a Integer as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, internal, description});
    }

    /**
     * This creates an Setting with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, int minimum, int maximum)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum});
    }

    /**
     * This creates an Setting with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, int minimum, int maximum, String description)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, description});
    }

    /**
     * This creates an Setting with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, int minimum, int maximum, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, internal});
    }

    /**
     * This creates an Setting with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(int defaultValue, int minimum, int maximum, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, internal, description});
    }

    //DOUBLE

    /**
     * This creates an Setting with a Double as default value.
     *
     * @param defaultValue The default value
     *
     * @return The new Setting
     */
    public static Setting newSetting(double defaultValue)
    {

        return newSetting(new Object[]{defaultValue});
    }

    /**
     * This creates an Setting with a Double as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, internal});
    }

    /**
     * This creates an Setting with a Double as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     *
     * @return The new Setting
     */
    public static Setting newSetting(double defaultValue, String description)
    {

        return newSetting(new Object[]{defaultValue, description});
    }

    /**
     * This creates an Setting with a Double as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, internal, description});
    }

    /**
     * This creates an Setting with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, double minimum, double maximum)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum});
    }

    /**
     * This creates an Setting with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, double minimum, double maximum, String description)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, description});
    }

    /**
     * This creates an Setting with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, double minimum, double maximum, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, internal});
    }

    /**
     * This creates an Setting with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(double defaultValue, double minimum, double maximum, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, minimum, maximum, internal, description});
    }

    //BOOLEAN

    /**
     * This creates an Setting with a boolean as default value.
     *
     * @param defaultValue The default value.
     *
     * @return The new Setting
     */
    public static Setting newSetting(boolean defaultValue)
    {

        return newSetting(new Object[]{defaultValue});
    }

    /**
     * This creates an Setting with a boolean as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(boolean defaultValue, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, internal});
    }

    /**
     * This creates an Setting with a boolean as default value.
     *
     * @param defaultValue The default value.
     * @param description  This is the description for the given Setting
     *
     * @return The new Setting
     */
    public static Setting newSetting(boolean defaultValue, String description)
    {

        return newSetting(new Object[]{defaultValue, description});
    }

    /**
     * This creates an Setting with a boolean as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(boolean defaultValue, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, internal, description});
    }

    //ENUM
    /**
     * This creates an Setting with a Enum as default value.
     *
     * @param defaultValue The default value.
     *
     * @return The new Setting
     */
    public static Setting newSetting(Enum defaultValue)
    {

        return newSetting(new Object[]{defaultValue});
    }

    /**
     * This creates an Setting with a Enum as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     *
     * @return The new setting
     */
    public static Setting newSetting(Enum defaultValue, boolean internal)
    {

        return newSetting(new Object[]{defaultValue, internal});
    }

    /**
     * This creates an Setting with a Enum as default value.
     *
     * @param defaultValue The default value.
     * @param description  This is the description for the given Setting
     *
     * @return The new Setting
     */
    public static Setting newSetting(Enum defaultValue, String description)
    {

        return newSetting(new Object[]{defaultValue, description});
    }

    /**
     * This creates an Setting with a Enum as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     *
     * @return The new setting
     */
    public static Setting newSetting(Enum defaultValue, boolean internal, String description)
    {

        return newSetting(new Object[]{defaultValue, internal, description});
    }

    //Settings from Elements

    /**
     * This transforms an Object to an Setting with the same Type as the argument.
     *
     * @param setting The argument for the setting as String, Integer, Double or Boolean.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder firstArgument(Object setting)
    {

        if (setting instanceof String)
        {
            return new SettingsBuilder().setDefaultValue((String) setting);
        }
        if (setting instanceof Integer | setting instanceof Double | setting instanceof Boolean)
        {
            return new SettingsBuilder().setDefaultValue(String.valueOf(setting));
        }
        if (setting instanceof Enum)
        {
            return new SettingsBuilder().setDefaultValue(((Enum) setting).getDeclaringClass().getName() + "." + setting.toString());
        }
        if (setting instanceof Object[])
        {
            return new SettingsBuilder().setDefaultValue((Object[]) setting);
        }
        if (setting instanceof Integer[] | setting instanceof Double[] | setting instanceof Boolean[])
        {
            return new SettingsBuilder().setDefaultValue(String.valueOf(setting));
        }
        if (setting instanceof Enum[])
        {
            return new SettingsBuilder().setDefaultValue(((Enum) setting).getDeclaringClass().getName() + "." + setting.toString());
        }
        LOG.warning(bundle.getString("wrongTypeOneArg"));
        return new SettingsBuilder();
    }

    /**
     * This Methode interprets an Array with two Object in such a way that it creates an Setting.
     *
     * @param settings The array with two arguments.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder twoArguments(Object[] settings)
    {

        if (settings.length < 2)
        {
            LOG.warning(bundle.getString("lessThenTwoArgs"));
            return new SettingsBuilder();
        }
        SettingsBuilder builder = firstArgument(settings[0]);
        if (settings[1] instanceof Boolean)
        {
            builder.setInternalValue((Boolean) settings[1]);
        } else if (settings[1] instanceof String)
        {
            builder.setInformationText((String) settings[1]);
        } else
        {
            LOG.warning(bundle.getString("wrongTypeTwoArgs"));
        }
        return builder;
    }

    /**
     * This Methode interprets an Array with three Object in such a way that it creates an Setting.
     *
     * @param settings The array with three arguments.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder threeArguments(Object[] settings)
    {

        if (settings.length < 3)
        {
            LOG.warning(bundle.getString("lessThenThreeArgs"));
            return new SettingsBuilder();
        }
        SettingsBuilder builder = firstArgument(settings[0]);
        if (settings[0] instanceof Integer && settings[1] instanceof Integer && settings[2] instanceof Integer)
        {
            builder.setMinimumValue((Integer) settings[1]);
            builder.setMaximumValue((Integer) settings[2]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Double && settings[2] instanceof Double)
        {
            builder.setMinimumValue((Double) settings[1]);
            builder.setMaximumValue((Double) settings[2]);
        } else if (settings[1] instanceof Boolean && settings[2] instanceof String)
        {
            builder.setInternalValue((Boolean) settings[1]);
            builder.setInformationText((String) settings[2]);
        } else
        {
            LOG.warning(bundle.getString("wrongTypeThreeArgs"));
        }
        return builder;
    }

    /**
     * This Methode interprets an Array with four  Object in such a way that it creates an Setting.
     *
     * @param settings The array with four arguments.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder fourArguments(Object[] settings)
    {

        if (settings.length < 4)
        {
            LOG.warning(bundle.getString("lessThenFourArgs"));
            return new SettingsBuilder();
        }
        SettingsBuilder builder = firstArgument(settings[0]);
        if (settings[0] instanceof Integer && settings[1] instanceof Integer && settings[2] instanceof Integer)
        {
            builder.setMinimumValue((Integer) settings[1]);
            builder.setMaximumValue((Integer) settings[2]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Double && settings[2] instanceof Double)
        {
            builder.setMinimumValue((Double) settings[1]);
            builder.setMaximumValue((Double) settings[2]);
        } else
        {
            LOG.warning(bundle.getString("wrongTypeFourArgs"));
        }
        if (settings[3] instanceof String)
        {
            builder.setInformationText((String) settings[3]);
        } else if (settings[3] instanceof Boolean)
        {
            builder.setInternalValue((Boolean) settings[3]);
        }
        return builder;
    }

    /**
     * This Methode interprets an Array with five Object in such a way that it creates an Setting.
     *
     * @param settings The array with five arguments.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder fiveArguments(Object[] settings)
    {
        if (settings.length < 5)
        {
            LOG.warning(bundle.getString("lessThenFiveArgs"));
            return new SettingsBuilder();
        }
        SettingsBuilder builder = threeArguments(new Object[]{settings[0], settings[1], settings[2]});
        if (settings[3] instanceof Boolean && settings[4] instanceof String)
        {
            builder.setInternalValue((Boolean) settings[3]);
            builder.setInformationText((String) settings[4]);
        } else
        {
            LOG.warning(bundle.getString("wrongTypeFiveArgs"));
        }
        return builder;
    }

    //LOAD and SAVE

    /**
     * This loads all values for the Settings in the given Class.
     *
     * @param settings The Class with the settings.
     *
     * @return true, if the settings got loaded successfully, otherwise false.
     */
    public boolean load(Object settings)
    {

        return STORAGE.load(getSettingsFromObject(settings));
    }

    /**
     * This Methode saves the Settings from the given Class.
     *
     * @param settings This ist the Class with the settings.
     *
     * @return true, if saving was successful, otherwise false.
     */
    public boolean save(Object settings)
    {

        return STORAGE.save(getSettingsFromObject(settings));
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
                result = STORAGE.save(getSettingsFromObject(Class.forName(classObj)));
            } catch (ClassNotFoundException e)
            {
                LOG.warning(bundle.getString("classToSaveNotFound_start") + classObj + bundle.getString("classToSaveNotFound_end"));
                result = false;
            }

        }
        return result;
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
                List<Setting> settings = getSettingsFromObject(Class.forName(className));
                if (!settings.isEmpty())
                {
                    result = STORAGE.load(settings) && result;
                }
            } catch (ClassNotFoundException e)
            {
                LOG.warning(bundle.getString("classToSaveNotFound_start") + className + bundle.getString("classToSaveNotFound_end"));
                result = false;
            }
        }
        return result;
    }

    //OTHER functions

    /**
     * This returns the given ResourceBundle or null if no ResourceBundle was given.
     *
     * @return The given ResourceBundle or null if no ResourceBundle was given.
     */
    public static ResourceBundle getResourceBundle()
    {

        return RESOURCE_BUNDLE;
    }

    /**
     * This loads the settings for the ConfigClasses.
     */
    private void loadOwnSettings()
    {

        if (!load(SettingsConfig.class))
        {
            save(SettingsConfig.class);//If the Settings aren't saved and the user program doesn't save all
        }
        language.SETTINGProperty().addListener(SettingsListener.getLanguageChangeListener());
    }
}
