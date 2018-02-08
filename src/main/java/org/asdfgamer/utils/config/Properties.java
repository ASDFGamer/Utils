package org.asdfgamer.utils.config;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;

import static org.asdfgamer.utils.other.Utils.createFile;
import static org.asdfgamer.utils.other.Utils.isFile;

/**
 * This class saves the given values in a file. It can also add Captions and Comments to specific values.
 * It cannot load settings, because it saves the settings in the same format like the original
 * {@link java.util.Properties} class and that class should be used to load the values.
 * <p>
 * Info: This class is in english and not in german, because I changed my mind about the language that should be used in
 * this project. Ideally some the whole project gets translated.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class Properties
{

    private static Locale locale = Locale.getDefault();

    private final static ResourceBundle bundle = ResourceBundle.getBundle("config/SettingClassInfo", locale);

    /**
     * This ist the filename of the file in which the Properties get written.
     */
    private final String file;

    private List<Map.Entry<String, String>> content = new LinkedList<>();

    /**
     * This creates an new Properties Object witch can save Settings.
     *
     * @param fileName This is the path to the Properties-File.
     */
    public Properties(String fileName)
    {

        file = fileName;
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

    /**
     * This Methode returns a File-Object of the specified fileName. If the file doesn't exist it gets created.
     *
     * @param fileName The Name of the File as absolute Path.
     * @return The File for the Path.
     */
    private File getFile(String fileName)
    {

        if (!isFile(fileName))
        {
            createFile(fileName);
        }
        return new File(fileName);//This should'n throw an exception
    }

    /**
     * This Method add an new Value from the given SettingsProperty to the end of the File.
     *
     * @param name    The Name of the Setting
     * @param setting The Setting that should be saved.
     * @return true, if the value got successfully added, otherwise false.
     */
    public boolean add(String name, SettingsProperty setting)
    {

        String value = "#" + setting.getInformationText() + "(" + bundle.getString("std_value") + " = " + setting.getDefaultValue() + ")" + "\n"
                + name + "=" + setting.get();
        Map.Entry<String, String> entry = new AbstractMap.SimpleEntry<String, String>(name, value);
        return content.add(entry);
    }

    /**
     * This Method adds a Map of Settings to the list of Properties that should be saved ands sorts them afterwards.
     *
     * @param settings  A Map of names of Settings and the associated SettingsProperty.
     * @param className The Name of the Class that should be used to sort the values.
     * @return true, if the Settings got successfully added and sorted, otherwise false.
     */
    public boolean add(Map<String, SettingsProperty> settings, Class className)
    {

        return add(settings) && sort(className);
    }

    /**
     * This Method adds a Map of Settings to the list of Properties that should be saved.
     *
     * @param settings A Map of names of Settings and the associated SettingsProperty.
     * @return true, if the Settings got successfully added, otherwise false.
     */
    public boolean add(Map<String, SettingsProperty> settings)
    {

        boolean result = true;
        for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
        {
            result = add(setting.getKey(), setting.getValue()) && result;
        }
        return result;
    }

    /**
     * This Method saves all values that got added to the class.
     *
     * @return true, if the file got saved successfully, otherwise false.
     */
    public boolean save()
    {

        return false;
    }

    /**
     * This Methode sorts all values in the order of the declaration in the given Class. For the order it searches in
     * the given class
     * important: if captions got added than they will be removed. New captions will get added in the positions of the
     * caption Annotation.
     *
     * @param className The Class that gets used to sort the Settings.
     * @return true, if the values got sorted successfully, otherwise false.
     */
    public boolean sort(Class className)
    {

        if (className.isEnum())
        {
            return sortEnum(className);
        } else
        {
            return sortClass(className);
        }
    }

    /**
     * This sorts the settings in content in the order they are declared in the given Class.
     * If there are settings in content that are nor of the given Class the get added after the sorted settings and this
     * Methode returns false.
     *
     * @param className The Class that should be used to sort the settings.
     * @return true, if every element got sorted, otherwise false.
     */
    private boolean sortClass(Class className)
    {

        Set<Map.Entry<String, String>> unsorted = new HashSet<>(content);
        List<Map.Entry<String, String>> sorted = new LinkedList<>();
        Field[] fieldsunsorted = className.getFields();
        Field[] fieldssorted = sortFields(fieldsunsorted);

        for (int i = 0; i < fieldssorted.length; i++)
        {
            Iterator<Map.Entry<String, String>> iterator = unsorted.iterator();
            boolean end = false;
            while (iterator.hasNext() && !end)
            {
                Map.Entry<String, String> element = iterator.next();
                if (element.getKey().equals(fieldssorted[i].getName()))
                {
                    sorted.add(element);
                    unsorted.remove(element);
                    end = true;
                }
            }
        }
        if (!unsorted.isEmpty())
        {
            sorted.addAll(unsorted);
        }
        content = sorted;
        return unsorted.isEmpty();
    }

    private Field[] sortFields(Field[] fieldsunsorted)
    {
        Field[] sorted = new Field[fieldsunsorted.length];
        boolean unsorted = true;
        Field temp;
        while (unsorted)
        {
            for (int i = 0; i < fieldsunsorted.length; i++)
            {
                unsorted = false;

                //if(fieldsunsorted[i]. > fieldsunsorted[i+1].)
                {
                    temp = fieldsunsorted[i];
                    fieldsunsorted[i] = fieldsunsorted[i+1];
                    fieldsunsorted[i+1] = temp;
                    unsorted = true;
                }
            }
        }
        return  null;
    }


    private boolean sortEnum(Class className)
    {

        return false;
    }
}
