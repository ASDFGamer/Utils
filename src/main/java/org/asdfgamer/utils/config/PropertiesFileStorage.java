package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.asdfgamer.utils.config.SettingUtils.bundle;
import static org.asdfgamer.utils.config.SettingUtils.sortSettingsInClasses;

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
    private static final String STD_EXTENSION = ".cfg";

    /**
     * This is the name of the program that saves the Settings.
     */
    private final String PROGRAM_NAME;

    /**
     * This indicates what implementation should be used to save the Properties:
     * true = {@link org.asdfgamer.utils.config.Properties}.
     * false = {@link java.util.Properties}.
     * This implementation is only used while saving. To load properties this class always uses {@link java.util.Properties}
     */
    private final boolean MY_PROPERTIES;

    /**
     * This shows if the Settings should be searched for changes, or if all settings should be saved.
     */
    private boolean checkForChanges = false;

    /**
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
     * @param settings This are all settings that should be loaded as List.
     * @return false, if there was an error while loading, otherwise true.
     */
    @Override
    public boolean load(List<SettingsProperty> settings)
    {

        boolean result = true;
        Map<String, List<SettingsProperty>> classes = sortSettingsInClasses(settings);
        for (Map.Entry<String, List<SettingsProperty>> settingsSortedInClasses : classes.entrySet())
        {
            String name = settingsSortedInClasses.getKey().substring(settingsSortedInClasses.getKey().lastIndexOf('.') + 1);
            String path = createFile(name);

                result = loadSettingProperties(settingsSortedInClasses.getValue(), path) && result;

        }
        return result;
    }

    /**
     * This methode lets you save Settings.
     *
     * @param settings This are all settings that should be saved as List.
     * @return true, if saving the settings was successful, otherwise false.
     */
    @Override
    public boolean save(List<SettingsProperty> settings)
    {

        boolean result = true;
        Map<String, List<SettingsProperty>> classes = sortSettingsInClasses(settings);
        for (Map.Entry<String, List<SettingsProperty>> settingsSortedInClasses : classes.entrySet())
        {
            String name = settingsSortedInClasses.getKey().substring(settingsSortedInClasses.getKey().lastIndexOf('.') + 1);
            String path = createFile(name);

            if (MY_PROPERTIES)
            {
                result = saveMYSettingProperties(settingsSortedInClasses.getValue(),path) && result;
            }else
            {
                result = saveSettingsProperty(settingsSortedInClasses.getValue(), path) && result;
            }
        }
        return result;

    }

    /**
     * @param value
     * @param path
     * @return
     */
    private boolean saveMYSettingProperties(List<SettingsProperty> value, String path)
    {

        org.asdfgamer.utils.config.Properties myProperties = new org.asdfgamer.utils.config.Properties(path, PROGRAM_NAME);
        myProperties.add(value);
        return myProperties.save();
    }

    /**
     * This saves all SettingsProperties from the given Map to the given file.
     *
     * @param settings This are all settings that should be saved as List.
     * @param path     This is the Path to the Config-file that should be used.
     * @return true, if there was no Problem during saving otherwise false.
     */
    private boolean saveSettingsProperty(List<SettingsProperty> settings, String path)
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
            for (SettingsProperty setting : settings)
            {
                if (!setting.isInternalValue())
                {
                    properties.setProperty(setting.getSettingName(), setting.get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), bundle.getString("fileHeader")+ PROGRAM_NAME + "'.");
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, bundle.getString("problemOpen"), e);
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
                    LOG.log(Level.SEVERE, bundle.getString("problemClose"), e);
                }
            }
        }
        return true;
    }

    /**
     * This checks if there was no change in the whole Map.
     *
     * @param settings This are all settings that should be checked as List.
     * @return true, if there was no Change, otherwise false.
     */
    private boolean noChanges(List<SettingsProperty> settings)
    {

        boolean settingChanged = false;

        for (SettingsProperty setting : settings)
        {
            if (setting.getSettingChanged())
            {
                settingChanged = true;
            }
        }

        if (!settingChanged)
        {
            LOG.info(bundle.getString("nothingChanged"));
            return true;
        }
        return false;
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
     * @param settings This are all settings that should be loaded as List.
     * @param path          The path to the file.
     * @return true, if the settings got loaded successful.
     */
    private boolean loadSettingProperties(List<SettingsProperty> settings, String path)
    {

        boolean result = true;
        Properties properties = new Properties();
        InputStream configFile = null;

        //remove change listener
        for (SettingsProperty setting : settings)
        {
            if (!setting.isInternalValue())
            {
                setting.removeListener(SettingsListener.getSettingChange(setting));
            }
        }
        //Load the settings
        try
        {
            configFile = new FileInputStream(path);
            properties.load(configFile);

            for (SettingsProperty setting : settings)
            {
                if (properties.getProperty(setting.getSettingName()) == null && !setting.isInternalValue())
                {
                    result = false;
                    LOG.warning(bundle.getString("problemLoadSetting_start") + " '" + setting.getSettingName() + "' " + bundle.getString("problemLoadSetting_end"));
                    SettingClassInfo.setProblemsWithLoading(setting.getClassName());
                }
                setting.set(properties.getProperty(setting.getSettingName(), setting.getDefaultValue()));
                SettingClassInfo.setSettingsLoaded(setting.getClassName());//This gets called to much -> performance loss
            }
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, bundle.getString("problemLoadFile"), e);
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
                    LOG.log(Level.SEVERE, bundle.getString("problemClose"), e);
                }
            }
        }

        //add the change listener again
        for (SettingsProperty setting : settings)
        {
            if (!setting.isInternalValue())
            {
                setting.addListener(SettingsListener.getSettingChange(setting));
            }
        }

        return result;
    }

}
