package org.asdfgamer.utils.config;

import org.asdfgamer.utils.config.sort.ListElement;
import org.asdfgamer.utils.config.sort.SettingsSorter;
import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * This class saves the given values in a file. It can also add Captions and Comments to specific values.
 * It cannot load settings, because it saves the settings in the same format like the original
 * {@link java.util.Properties} class and that class should be used to load the values.
 */

@SuppressWarnings("WeakerAccess")
public class Properties
{

    /**
     * This is the used Logger
     */
    private final static Logger LOG = getLogger(Properties.class.getName());

    /**
     * This is the locale for the used Language
     */
    private static Locale locale = Locale.getDefault();

    /**
     * This is the resource Bundle for the Strings that need to be translated
     */
    private final static ResourceBundle bundle = ResourceBundle.getBundle("config/Settings", locale);//TODO test if a change in language works

    /**
     * This ist the filename of the file in which the Properties get written.
     */
    private final String file;

    /**
     * This is the used sorter to sort all Settings in the right order.
     */
    private SettingsSorter settingsSorter = new SettingsSorter();

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
    public static void setLocale(Locale newLocale)
    {

        locale = newLocale;
    }

    /**
     * This Method add an new Value from the given SettingsProperty to the end of the File.
     *
     * @param name    The Name of the Setting
     * @param setting The Setting that should be saved.
     */
    public void add(String name, SettingsProperty setting)
    {

        settingsSorter.add(name, setting);
    }

    /**
     * This Method adds a Map of Settings to the list of Properties that should be saved ands sorts them afterwards.
     *
     * @param settings  A Map of names of Settings and the associated SettingsProperty.
     * @param className The Name of the Class that should be used to sort the values.
     */
    @SuppressWarnings("unused")
    public void add(Map<String, SettingsProperty> settings, Class className)
    {

        add(settings);
        settingsSorter.add(className);
    }

    /**
     * This Method adds a Map of Settings to the list of Properties that should be saved.
     *
     * @param settings A Map of names of Settings and the associated SettingsProperty.
     */
    public void add(Map<String, SettingsProperty> settings)
    {

        for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
        {
            add(setting.getKey(), setting.getValue());
        }
    }

    /**
     * This Method saves all values that got added to the class.
     *
     * @return true, if the file got saved successfully, otherwise false.
     */
    public boolean save()
    {

        List<ListElement> content = settingsSorter.getSortedList();
        if (!Utils.isFile(file))
        {
            if (!Utils.createFile(file))
            {
                LOG.warning(bundle.getString("couldntCreateFile") + file);
                return false;
            }
        }
        try
        {
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
            for (ListElement element : content)
            {
                writer.write(element.getContent());//TODO check if this clears the file.
            }
            writer.flush();
        } catch (IOException e)
        {
            LOG.warning(bundle.getString("problemWhileWriting"));
            return false;
        }
        return true;
    }

}
