package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class lets you save Settings in a Properties-File.
 * This file is saved to the OS-dependent configuration-folder.
 * In Windows this is '%AppData%\Roaming\"PROGRAM_NAME"', in Linux '.config/"PROGRAM_NAME"' and
 * in other Systems a folder directly inside the User-folder.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PropertiesFileStorage implements SettingsStorage
{

    /**
     * The used Logger.
     */
    private static final Logger LOG = Logger.getLogger(PropertiesFileStorage.class.getName());

    /**
     * This is the file extension if there is no Extension given. TODO add possibility to change the extension.
     */
    private static final String STD_EXTENSION = ".properties";

    /**
     * This is the name of the program that saves the Settings.
     */
    private final String PROGRAM_NAME;

    /**
     * This indicates what implementation should be used to save the Properties:
     * true = {@link java.util.Properties}.
     * false = {@link org.asdfgamer.utils.config.Properties}.
     * This implementation is only used while saving. To load properties this class always uses {@link java.util.Properties}
     */
    private final boolean MY_PROPERTIES;

    /**
     * This shows if the Settings should be searched for changes, or if all settings should be saved.
     */
    private boolean checkForChanges = false;

    /**
     * TODO translate
     * This creates a new SettingsStorage for the given Program name.
     * This object uses all default values.
     *
     * @param programName The name of the program that saves the settings.
     */
    public PropertiesFileStorage(String programName)
    {

        this.PROGRAM_NAME = programName;
        this.MY_PROPERTIES = true;
    }

    /**
     * This creates a new SettingsStorage with the given arguments.
     *
     * @param programName         Tha name of the program that saves the settings.
     * @param checkForChanges     This shows if the Settings should only be saved if they changed.
     *                            By default this is false.TODO change to true
     * @param useBetterProperties This is used to choose the way the Properties get saved into the file. If this is true it
     *                            uses a implementation of the Properties that can write comments, and a few other handy
     *                            things. If it is false it uses the default implementation to save the properties.
     */
    public PropertiesFileStorage(String programName, boolean checkForChanges, boolean useBetterProperties)
    {

        this.PROGRAM_NAME = programName;
        this.checkForChanges = checkForChanges;
        this.MY_PROPERTIES = useBetterProperties;
    }

    /**
     * This Methode loads all Settings that are present in the map.
     *
     * @param settings This are all settings that should be loaded as Map. The Key is the Name of the setting and the
     *                 value is the setting itself.
     * @return false, if there was an error while loading, otherwise true.
     */
    @Override
    public boolean load(Map<String, SettingsProperty> settings)
    {

        boolean result = true;
        Map<String, Map<String, SettingsProperty>> classes = sortiereEinstellungenNachKlassen(settings);
        for (Map.Entry<String, Map<String, SettingsProperty>> settingsSortedInClasses : classes.entrySet())
        {
            String name = settingsSortedInClasses.getKey().substring(settingsSortedInClasses.getKey().lastIndexOf('.') + 1);
            String path = createFile(name);

            result = loadSettingProperies(settingsSortedInClasses.getValue(), path) && result;
        }
        return result;
    }

    /**
     * This methode lets you save Settings.
     *
     * @param settings This are all settings that should be saved as Map. The Key is the Name of the setting and the
     *                 value is the setting itself.
     * @return true, if saving the settings was successful, otherwise false.
     */
    @Override
    public boolean save(Map<String, SettingsProperty> settings)
    {

        boolean result = true;
        Map<String, Map<String, SettingsProperty>> klassen = sortiereEinstellungenNachKlassen(settings);
        for (Map.Entry<String, Map<String, SettingsProperty>> settingsSortedInClasses : klassen.entrySet())
        {
            String name = settingsSortedInClasses.getKey().substring(settingsSortedInClasses.getKey().lastIndexOf('.') + 1);
            String path = createFile(name);

            result = saveSettingsProperty(settingsSortedInClasses.getValue(), path) && result;

        }
        return result;

    }

    /**
     * This saves all SettingsProperties from the given Map to the given file.
     *
     * @param settings This are all settings that should be saved as Map. The Key is the Name of the setting and the
     *                 value is the setting itself.
     * @param path     This is the Path to the Config-file that should be used.
     * @return true, if there was no Problem during saving otherwise false.
     */
    private boolean saveSettingsProperty(Map<String, SettingsProperty> settings, String path)
    {

        if (checkForChanges)
        {
            if (noChanges(settings))
            {
                return true;
            }
        }

        Properties properties = new Properties();
        OutputStream configFile = null;

        try
        {
            configFile = new FileOutputStream(path);
            for (Map.Entry<String, SettingsProperty> einstellung : settings.entrySet())
            {
                if (!einstellung.getValue().isInternalValue())
                {
                    LOG.info("speichern " + einstellung.getKey());
                    properties.setProperty(einstellung.getKey(), einstellung.getValue().get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), "Dies sind die Einstellungen des Spiels '" + PROGRAM_NAME + "'.");
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Es gab Probleme beim Öffnen der Datei zum Speichern der Einstellungen", e);
            return false;
        } finally
        {
            if (configFile != null)
            {
                try
                {
                    configFile.close();
                } catch (IOException e)
                {
                    LOG.log(Level.SEVERE, "Die Configfile konnte nicht geschlossen werden.", e);
                }
            }
        }
        return true;
    }

    /**
     * This checks if there was no change in the whole Map.
     *
     * @param settings This are all settings that should be checked as Map. The Key is the Name of the setting and the
     *                 value is the setting itself.
     * @return true, if there was no Change, otherwise false.
     */
    private boolean noChanges(Map<String, SettingsProperty> settings)
    {

        boolean settingChanged = false;

        for (Map.Entry<String, SettingsProperty> entry : settings.entrySet())
        {
            if (entry.getValue().getSettingChanged())
            {
                settingChanged = true;
            }
        }

        if (!settingChanged)
        {
            LOG.info("Es wurde keine Einstellung geändert, deshalb muss nichts gespeichert werden.");
            return true;
        }
        return false;
    }

    /**
     * This creates a nested Map which has in the first map the Classname and in the second Map the name of the setting
     * an the setting itself.
     *
     * @param settings This are all settings that should be sorted as Map. The Key is the Name of the setting and the
     *                value is the setting itself. The settings can be from different classes but don't have to be.
     * @return A nested Map which has in the first map the Classname and in the second Map the name of the setting an
     * the setting itself.
     */
    private Map<String, Map<String, SettingsProperty>> sortiereEinstellungenNachKlassen(Map<String, SettingsProperty> settings)
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
     * This Methode searches for  a File with the given Name. If it exists it returns the path ro it and if it doesn't
     * it attempts to create the file. If this is successful this will also return the path and if it can't create the
     * file it returns null.
     * @param fileName The name of the file that should be created.
     * @return The path to the config file or 'null' if the file doesn't exists and can't be created.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private String createFile(String fileName)
    {

        return Utils.getConfigFile(PROGRAM_NAME, fileName + STD_EXTENSION);//TODO check if the filename has already an extension
    }

    /**
     * This loads all SettingsProperties from a Class.
     * All Settings that are given must be from the same class.
     *
     * @param settings This are all settings that should be loaded as Map. The Key is the Name of the setting and the
     *                value is the setting itself. The settings can be from different classes but don't have to be.
     * @param path          The path to the file.
     * @return true, if the settings got loaded successful.
     */
    private boolean loadSettingProperies(Map<String, SettingsProperty> settings, String path)
    {

        boolean result = true;
        Properties properties = new Properties();
        InputStream configFile = null;

        //remove change listener
        for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
        {
            if (!setting.getValue().isInternalValue())
            {
                setting.getValue().removeListener(SettingsListener.getSettingChange(setting.getValue()));
            }
        }
        //Load the settings
        try
        {
            configFile = new FileInputStream(path);
            properties.load(configFile);

            for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
            {
                if (properties.getProperty(setting.getKey()) == null && !setting.getValue().isInternalValue())
                {
                    result = false;
                    LOG.warning("Für die Einstellung " + setting.getKey() + " konnte kein Wert geladen werden.");
                    SettingClassInfo.setProblemsWithLoading(setting.getValue().getClassName());
                }
                setting.getValue().set(properties.getProperty(setting.getKey(), setting.getValue().getDefaultValue()));
                SettingClassInfo.setSettingsLoaded(setting.getValue().getClassName());//This gets called to much -> performance loss
            }
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Die Configfile konnte nicht geladen werden.", e);
            result = false;
        } finally
        {
            //Close the setting file
            if (configFile != null)
            {
                try
                {
                    configFile.close();
                } catch (IOException e)
                {
                    LOG.log(Level.SEVERE, "Die Configfile konnte nicht geschlossen werden.", e);
                }
            }
        }

        //add the change listener again
        for (Map.Entry<String, SettingsProperty> setting : settings.entrySet())
        {
            if (!setting.getValue().isInternalValue())
            {
                setting.getValue().addListener(SettingsListener.getSettingChange(setting.getValue()));
            }
        }

        return result;
    }

}
