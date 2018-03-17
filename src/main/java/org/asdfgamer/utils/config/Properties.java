package org.asdfgamer.utils.config;

import org.asdfgamer.utils.config.sort.ListElement;
import org.asdfgamer.utils.config.sort.SettingElement;
import org.asdfgamer.utils.config.sort.SettingsSorter;
import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.List;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;

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
     * This ist the filename of the file in which the Properties get written.
     */
    private final String file;

    /**
     * This is the Name of the Program.
     */
    private final String PROGRAM_NAME;

    /**
     * This is the used sorter to sort all Settings in the right order.
     */
    private SettingsSorter settingsSorter = new SettingsSorter();

    private Class className = null;

    /**
     * This creates an new Properties Object witch can save Settings.
     *
     * @param fileName    This is the path to the Properties-File.
     * @param programName The Name of the Program.
     */
    public Properties(String fileName, String programName)
    {

        PROGRAM_NAME = programName;
        file = fileName;
    }


    /**
     * This Method add an new Value from the given SettingsProperty to the end of the File.
     *
     * @param setting The Setting that should be saved.
     */
    public void add(SettingsProperty setting)
    {
        if (!setting.isInternalValue())
        {
            settingsSorter.add(setting);
            if (className == null)
            {
                try
                {
                    className = Class.forName(setting.getClassName());
                } catch (ClassNotFoundException e)
                {
                    LOG.warning(bundle.getString("classNotFound") + setting.getClassName());
                    className = null;
                }
            }
        }
    }

    /**
     * This Method adds a Map of Settings to the list of Properties that should be saved.
     *
     * @param settings A Map of names of Settings and the associated SettingsProperty.
     */
    public void add(List<SettingsProperty> settings)
    {

        for (SettingsProperty setting : settings)
        {
            add(setting);
        }
        settingsSorter.add(className);
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
            writer.write("#" + bundle.getString("fileHeader") + PROGRAM_NAME + "'.");
            writer.write("\n");

            for (ListElement element : content)
            {
                if (element instanceof SettingElement)
                {
                    SettingsProperty setting = ((SettingElement) element).getSetting();
                    String settingText;
                    if (setting.getType().equals(SettingsPropertyTypes.String))
                    {
                        settingText = "\"" + setting.get() + "\"";
                    } else
                    {
                        settingText = setting.get();
                    }
                    writer.write("#" + setting.getInformationText() + "(" + bundle.getString("std_value") + " = " + setting.getDefaultValue() + ")" + "\n" + setting.getSettingName() + "=" + settingText + "\n");
                } else//CaptionElement
                {
                    writer.write("\n#" + element.getContent() + "\n");
                }
                writer.write("\n");
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
