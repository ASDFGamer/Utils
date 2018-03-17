package org.asdfgamer.utils.config.tests;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class Main
{
    private final static Logger LOG = getLogger(Main.class.getName());

    public static void main(String[] args)
    {
        SettingsTest settingsTest = new SettingsTest();
        settingsTest.newSetting();
        PropertiesFileStorageTest propertiesFileStorageTest = new PropertiesFileStorageTest();
        propertiesFileStorageTest.loadAllSettings();
        propertiesFileStorageTest.loadSettings();
        propertiesFileStorageTest.loadSettingsFromEnum();
        GUI.main(args);
    }

}
