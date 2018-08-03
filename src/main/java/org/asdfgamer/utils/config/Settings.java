package org.asdfgamer.utils.config;

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
     * @param storage     The Method that saves the Settings persistent.
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
     * @param storage        The Method that saves the Settings persistent.
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

    public static Setting newSetting(Object arg)
    {
        return newSetting(new Object[]{arg});
    }

    public static Setting newSetting(Object[] args)
    {
        if (args == null || args.length == 0)
        {
            return newSetting();
        }
        SettingsBuilder settingsBuilder;
        if (args.length > 1)
        {
            Class aClass = args[0].getClass();
            for (int i = 1; i < args.length - 1; i++)
            {
                if (!args[i].getClass().equals(aClass))
                {
                    throw new IllegalArgumentException(bundle.getString("AllSameType"));
                }
            }
            settingsBuilder = parseElements(args);
        } else
        {
            settingsBuilder = parseArgument(args[0]);
        }
        return settingsBuilder.build();
    }

    //Settings from Elements

    /**
     * This transforms an Object to an Setting with the same Type as the argument.
     *
     * @param setting The argument for the setting as String, Integer, Double or Boolean.
     *
     * @return The new Setting.
     */
    private static SettingsBuilder parseArgument(Object setting)
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
            return parseElements((Object[]) setting);
        }
        LOG.warning(bundle.getString("wrongTypeOneArg"));
        return new SettingsBuilder();
    }

    private static SettingsBuilder parseElements(Object[] settings)
    {
        String[] settingStrings = new String[settings.length];
        for (int i = 0; i < settings.length; i++)
        {
            if (settings[i] instanceof String)
            {
                settingStrings[i] = (String) settings[i];
            }
            if (settings[i] instanceof Integer | settings[i] instanceof Double | settings[i] instanceof Boolean)
            {
                settingStrings[i] = String.valueOf(settings[i]);
            }
            if (settings[i] instanceof Enum)
            {
                settingStrings[i] = ((Enum) settings[i]).getDeclaringClass().getName() + "." + settings[i].toString();
            }
        }
        return new SettingsBuilder().setDefaultValue(settingStrings);
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
     * This Method saves the Settings from the given Class.
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
     * This Method saves all Settings to the given Storage.
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
