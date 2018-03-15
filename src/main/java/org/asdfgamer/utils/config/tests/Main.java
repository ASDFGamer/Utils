package org.asdfgamer.utils.config.tests;

import org.asdfgamer.utils.other.Utils;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class Main
{
    private final static Logger LOG = getLogger(Main.class.getName());

    public static void main(String[] args)
    {
        SettingsTest settingsTest = new SettingsTest();
        settingsTest.newSetting();
        try
        {
            LOG.warning(Utils.isEnum(Class.forName("org.asdfgamer.utils.config.SettingsConfig.language")) + "");
        } catch (ClassNotFoundException e)
        {
            LOG.warning("geht nicht mit string to enum");
        }
        PropertiesFileStorageTest propertiesFileStorageTest = new PropertiesFileStorageTest();
        propertiesFileStorageTest.loadAllSettings();
        propertiesFileStorageTest.loadSettings();
        propertiesFileStorageTest.loadSettingsFromEnum();
        GUI.main(args);
    }

}
