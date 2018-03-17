package org.asdfgamer.utils.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.asdfgamer.utils.config.internal.SettingUtils.bundle;

/**
 * In this Class are different information about the whole Class/Enum with Settings, because they are for the Class/Enum
 * and not for a specific SettingsProperty.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class SettingClassInfo
{

    /**
     * This ist the used logger.
     */
    private final static Logger LOG = getLogger(SettingClassInfo.class.getName());

    /**
     * This Map is the Map with the info to every relevant Class.
     */
    private final static Map<String, ClassInfo> info = new HashMap<>();


    /**
     * This is used to show that the Class is changed.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     */
    public static void setChanged(String className)
    {

        if (info.containsKey(className))
        {
            info.get(className).changed = true;
        } else
        {
            ClassInfo classInfo = new ClassInfo();
            classInfo.changed = true;
            info.put(className, classInfo);
        }
    }

    /**
     * This saves how many Settings are in a Class. This can't be changed.
     *
     * @param classname The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     * @param settings  This is the Number if Settings
     * @return true, if the Number can be set and otherwise false.
     */
    public static boolean setNumberOfSettings(String classname, int settings)
    {

        if (info.containsKey(classname))
        {
            if (info.get(classname).settings != 0)
            {
                LOG.warning(bundle.getString("NumberOfSettingsAlreadySet"));
                return false;
            }
            info.get(classname).settings = settings;

        } else
        {
            ClassInfo classInfo = new ClassInfo();
            classInfo.settings = settings;
            info.put(classname, classInfo);
        }
        return true;
    }

    /**
     * This returns if the given class is changed.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     * @return true, if a Setting in the class changed and false if nothing changed or no info about the class exists.
     */
    public static boolean isChanged(String className)
    {

        if (info.containsKey(className))
        {
            return info.get(className).changed;
        }
        LOG.warning(bundle.getString("noInfo_start") + className + bundle.getString("noInfo_end"));
        return false;
    }

    /**
     * This returns how many Settings are in the class.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     * @return The Number of Settings or 0 if there are no information about the class.
     */
    public static int getSettings(String className)
    {

        if (info.containsKey(className))
        {
            return info.get(className).settings;
        }
        LOG.warning(bundle.getString("noInfo_start") + className + bundle.getString("noInfo_end"));
        return 0;
    }

    /**
     * This marks for the given Class that the Class was already loaded.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     */
    public static void setSettingsLoaded(String className)
    {

        if (info.containsKey(className))
        {
            info.get(className).settingsLoaded = true;
        } else
        {
            ClassInfo classInfo = new ClassInfo();
            classInfo.settingsLoaded = true;
            info.put(className, classInfo);
        }

    }

    /**
     * This specifies that there was a Problem during loading of the Settings for this class.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     */
    public static void setProblemsWithLoading(String className)
    {

        if (info.containsKey(className))
        {
            info.get(className).settingsLoadedCompletely = false;
            info.get(className).settingsLoaded = true;

        } else
        {
            ClassInfo classInfo = new ClassInfo();
            classInfo.settingsLoadedCompletely = false;
            classInfo.settingsLoaded = true;
            info.put(className, classInfo);
        }
    }

    /**
     * This shows if there was a problem while loading with the given class.
     *
     * @param className The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     * @return true, if there was a problem, false if there was no problem or the settings weren't loaded.
     */
    public static boolean getProblemsWithLoading(String className)
    {

        if (info.containsKey(className))
        {
            if (info.get(className).settingsLoaded)
            {
                return !info.get(className).settingsLoadedCompletely;
            }
        }
        return false;
    }

    /**
     * This adds the given class to the saved Classes.
     *
     * @param classname The absolute Classname (e.g. org.asdfgamer.utils.config.SettingsEnum)
     */
    public static void add(String classname)
    {

        if (!info.containsKey(classname))
        {
            info.put(classname, new ClassInfo());
        }
    }

    /**
     * This returns a Set of all Classes with SettingsProperties.
     *
     * @return A Set of all Classes with SettingsProperties.
     */
    public static Set<String> getClasses()
    {

        return info.keySet();
    }


    /**
     * This is the Information that gets saved for a Class.
     */
    private static class ClassInfo
    {

        /**
         * This shows if a Setting in this Class got changed.
         */
        private boolean changed = false;

        /**
         * This shows how many Settings are in the Class.
         */
        private int settings = 0;

        /**
         * This shows if the settings of this class got already loaded.
         */
        private boolean settingsLoaded = false;

        /**
         * This indicates if all Settings were loaded successfully the last time.
         * If {@link ClassInfo#settingsLoaded}==false then is this field unimportant.
         */
        private boolean settingsLoadedCompletely = true;
    }
}
