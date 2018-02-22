package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.SettingUtils.bundle;
import static org.asdfgamer.utils.config.SettingUtils.getSettingsFromObject;
import static org.asdfgamer.utils.config.SettingsConfig.language;

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

        this.NAME = programName;
        this.STORAGE = storage;
        loadOwnSettings();
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
     *
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
            case 4:
                return fourArguments(args);
            default:
                LOG.warning(bundle.getString("toManyArgs") + Arrays.toString(args));
                return newSetting();

        }
    }

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
        LOG.warning(bundle.getString("wrongTypeOneArg"));
        return newSetting();
    }
//

    /**
     * This Methode interprets an Array with two Object in such a way that it creates an SettingsProperty.
     *
     * @param settings The array with two arguments.
     * @return The new Setting.
     */
    private static SettingsProperty twoArguments(Object[] settings)
    {

        if (settings.length < 2)
        {
            LOG.warning(bundle.getString("lessThenTwoArgs"));
            return newSetting();
        }

        if (settings[0] instanceof String && settings[1] instanceof Boolean)
        {
            return newSetting((String) settings[0], (Boolean) settings[1]);
        } else if (settings[0] instanceof String && settings[1] instanceof String)
        {
            return newSetting((String) settings[0], (String) settings[1]);
        } else if (settings[0] instanceof Integer && settings[1] instanceof Boolean)
        {
            return newSetting((Integer) settings[0], (Boolean) settings[1]);
        } else if (settings[0] instanceof Integer && settings[1] instanceof String)
        {
            return newSetting((Integer) settings[0], (String) settings[1]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Boolean)
        {
            return newSetting((Double) settings[0], (Boolean) settings[1]);
        } else if (settings[0] instanceof Double && settings[1] instanceof String)
        {
            return newSetting((Double) settings[0], (String) settings[1]);
        } else if (settings[0] instanceof Boolean && settings[1] instanceof Boolean)
        {
            return newSetting((Boolean) settings[0], (Boolean) settings[1]);
        } else if (settings[0] instanceof Boolean && settings[1] instanceof String)
        {
            return newSetting((Boolean) settings[0], (String) settings[1]);
        }
        LOG.warning(bundle.getString("wrongTypeTwoArgs"));
        return newSetting();
    }

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
            LOG.warning(bundle.getString("lessThenThreeArgs"));
            return newSetting();
        }

        if (settings[0] instanceof Integer && settings[1] instanceof Integer && settings[2] instanceof Integer)
        {
            return newSetting((Integer) settings[0], (Integer) settings[1], (Integer) settings[2]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Double && settings[2] instanceof Double)
        {
            return newSetting((Double) settings[0], (Double) settings[1], (Double) settings[2]);
        } else if (settings[0] instanceof String && settings[1] instanceof Boolean && settings[2] instanceof String)
        {
            return newSetting((String) settings[0], (Boolean) settings[1], (String) settings[2]);
        } else if (settings[0] instanceof Integer && settings[1] instanceof Boolean && settings[2] instanceof String)
        {
            return newSetting((Integer) settings[0], (Boolean) settings[1], (String) settings[2]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Boolean && settings[2] instanceof String)
        {
            return newSetting((Double) settings[0], (Boolean) settings[1], (String) settings[2]);
        } else if (settings[0] instanceof Boolean && settings[1] instanceof Boolean && settings[2] instanceof String)
        {
            return newSetting((Boolean) settings[0], (Boolean) settings[1], (String) settings[2]);
        }
        LOG.warning(bundle.getString("wrongTypeThreeArgs"));
        return newSetting();
    }
//

    /**
     * This Methode interprets an Array with four  Object in such a way that it creates an SettingsProperty.
     *
     * @param settings The array with four arguments.
     * @return The new Setting.
     */
    private static SettingsProperty fourArguments(Object[] settings)
    {

        if (settings.length < 4)
        {
            LOG.warning(bundle.getString("lessThenFourArgs"));
            return newSetting();
        }

        if (settings[0] instanceof Integer && settings[1] instanceof Integer && settings[2] instanceof Integer && settings[3] instanceof String)
        {
            return newSetting((Integer) settings[0], (Integer) settings[1], (Integer) settings[2], (String) settings[3]);
        } else if (settings[0] instanceof Double && settings[1] instanceof Double && settings[2] instanceof Double && settings[3] instanceof String)
        {
            return newSetting((Double) settings[0], (Double) settings[1], (Double) settings[2], (String) settings[3]);
        }
        LOG.warning(bundle.getString("wrongTypeFourArgs"));
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
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
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
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
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
     * This creates an SettingsProperty with a String as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     * @return The new Setting
     */
    public static SettingsProperty newSetting(String defaultValue, String description)
    {

        SettingsProperty property = new SettingsProperty(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);

        return property;
    }

    /**
     * This creates an SettingsProperty with a Integer as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     * @return The new Setting.
     */
    public static SettingsProperty newSetting(int defaultValue, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Double as default value.
     *
     * @param defaultValue The default value
     * @param description  This is the description for the given Setting
     * @return The new Setting
     */
    public static SettingsProperty newSetting(double defaultValue, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setDouble(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a boolean as default value.
     *
     * @param defaultValue The default value.
     * @param description  This is the description for the given Setting
     * @return The new Setting
     */
    public static SettingsProperty newSetting(boolean defaultValue, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setBoolean(defaultValue);
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a String as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(String defaultValue, boolean internal, String description)
    {

        SettingsProperty property = new SettingsProperty(defaultValue, internal);
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Integer as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(int defaultValue, boolean internal, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a Double as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(double defaultValue, boolean internal, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        property.setDouble(defaultValue);
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a boolean as default value.
     *
     * @param defaultValue The default Value
     * @param internal     This shows if the setting should only be used internally or if it should also be saved.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(boolean defaultValue, boolean internal, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue), internal);
        property.setBoolean(defaultValue);
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a integer as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(int defaultValue, int minimum, int maximum, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setMinimumValue(minimum);
        property.setMaximumValue(maximum);
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This creates an SettingsProperty with a double as default value.
     * Additionally this creates an area for the Setting.
     *
     * @param defaultValue The default value.
     * @param minimum      The lowest allowed value.
     * @param maximum      The highest allowed value.
     * @param description  This is the description for the given Setting
     * @return The new setting
     */
    public static SettingsProperty newSetting(double defaultValue, double minimum, double maximum, String description)
    {

        SettingsProperty property = new SettingsProperty(String.valueOf(defaultValue));
        property.setMinimumValue(minimum);
        property.setMaximumValue(maximum);
        property.addListener(SettingsListener.getSettingChange(property));
        property.setInformationText(description);
        addClass(property);
        return property;
    }

    /**
     * This adds the Class in which the setting ist declared to the list of all classes with settings.
     *
     * @param property The Setting.
     */
    private static void addClass(SettingsProperty property)
    {

        String className;
        if (objArrayCreator)
        {
            className = Thread.currentThread().getStackTrace()[5].getClassName();
            setLine(property, Thread.currentThread().getStackTrace()[6]);
            objArrayCreator = false;
        } else
        {
            className = Thread.currentThread().getStackTrace()[3].getClassName();
            setLine(property, Thread.currentThread().getStackTrace()[3]);
        }
        property.setClassName(className);
        SettingClassInfo.add(className);
    }

    /**
     * This gets the Line number of the property if it is can (the class is an enum) and saves it in the property.
     *
     * @param property The Property where the linenumber should be added.
     * @param element  The Element from the stacktrace.
     */
    private static void setLine(SettingsProperty property, StackTraceElement element)
    {

        try
        {
            if (Class.forName(element.getClassName()).isEnum())//Test if this can work with normal classes. (It cant find the declaration only the initialisation).
            {
                property.setLine(element.getLineNumber());
            }
        } catch (ClassNotFoundException e)
        {
            LOG.fine(bundle.getString("classNotFound") + element.getClassName() + "'.");
        }
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

    /**
     * This Methode saves the Settings from the given Class.
     *
     * @param settings This ist the Class with the settings.
     * @return true, if saving was successful, otherwise false.
     */
    public boolean save(Object settings)
    {

        return STORAGE.save(getSettingsFromObject(settings));
    }

    /**
     * This adds the Name of the Setting to the SettingProperty
     *
     * @param settings The Class with the settings.
     */
    private void setSettingNamesClass(Object settings)
    {

        Map<String, SettingsProperty> stringSettingsPropertyMap = Utils.getFields(settings);
        for (Map.Entry<String, SettingsProperty> entry : stringSettingsPropertyMap.entrySet())
        {
            entry.getValue().setSettingName(entry.getKey());
        }

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
     * This loads all values for the Settings in the given Class.
     *
     * @param settings The Class with the settings.
     * @return true, if the settings got loaded successfully, otherwise false.
     */
    public boolean load(Object settings)
    {

        return STORAGE.load(getSettingsFromObject(settings));
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
                List<SettingsProperty> settings = getSettingsFromObject(Class.forName(className));
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

}
