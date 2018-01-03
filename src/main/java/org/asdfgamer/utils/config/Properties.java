package org.asdfgamer.utils.config;

import java.io.File;
import java.util.Map;

import static org.asdfgamer.utils.other.Utils.*;

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

    /**
     * This ist the filename of the file in which the Properties get written.
     */
    private final String file;

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
        return new File(fileName);//Hier sollte nie eine Exception geworfen werden.
    }

    /**
     * This Method add an new Value from the given EinstellungenProperty to the end of the File.
     *
     * @param name    The Name of the Setting
     * @param setting The Setting that should be saved.
     * @return true, if the value got successfully added, otherwise false.
     */
    public boolean add(String name, EinstellungenProperty setting)
    {

        return false;
    }

    /**
     * This Method adds a Map of Settings to the list of Proerties that should be saved ands sorts them afterwards.
     *
     * @param settings  A Map of names of Settings and the associated EinstellungenProperty.
     * @param className The Name of the Class that should be used to sort the values.
     * @return true, if the Settings got successfully added and sorted, otherwise false.
     */
    public boolean add(Map<String, EinstellungenProperty> settings, Class className)
    {

        return add(settings) && sort(className);
    }

    /**
     * This Method adds a Map of Settings to the list of Proerties that should be saved.
     *
     * @param settings A Map of names of Settings and the associated EinstellungenProperty.
     * @return true, if the Settings got successfully added, otherwise false.
     */
    public boolean add(Map<String, EinstellungenProperty> settings)
    {

        return false;
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
     * importand: if captions got added than they will be removed. New captions will get added in the positions of the
     * caption Annotation.
     *
     * @param className The Class that gets used to sort the Settings.
     * @return true, if the values got sorted successfully, otherwise false.
     */
    public boolean sort(Class className)
    {

        return false;
    }
}
