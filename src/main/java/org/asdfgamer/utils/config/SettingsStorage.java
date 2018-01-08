package org.asdfgamer.utils.config;

import java.util.Map;

/**
 * This Interface is used to give different storage possibilities for the settings.
 *
 * @author ASDFGamer
 * @version 1.0
 */
public interface SettingsStorage
{

    /**
     * This loads all Settings from the Map.
     *
     * @param settings This is a Map, with the name of the setting as key and the setting itself as value.
     * @return true, if there was no problem while loading the Settings, otherwise false.
     */
    boolean load(Map<String, SettingsProperty> settings);

    /**
     * This saves all Settings from the Map.
     *
     * @param settings This is a Map, with the name of the setting as key and the setting itself as value.
     * @return true, if there was no problem while saving the Settings, otherwise false.
     */
    boolean save(Map<String, SettingsProperty> settings);

}
