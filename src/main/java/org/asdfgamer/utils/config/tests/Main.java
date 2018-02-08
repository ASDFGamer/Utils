package org.asdfgamer.utils.config.tests;

public class Main
{

    public static void main(String[] args)
    {
        SettingsTest settingsTest = new SettingsTest();
        settingsTest.newSetting();

        PropertiesFileStorageTest propertiesFileStorageTest = new PropertiesFileStorageTest();
        propertiesFileStorageTest.loadAllSettings();
        propertiesFileStorageTest.loadSettings();
        propertiesFileStorageTest.loadSettingsFromEnum();
    }

}
