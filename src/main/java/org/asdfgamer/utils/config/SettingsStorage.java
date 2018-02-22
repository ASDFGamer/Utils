package org.asdfgamer.utils.config;

import java.util.List;

/**
 * This Interface is used to give different storage possibilities for the settings.
 *
 * @author ASDFGamer
 * @version 1.0
 */
@SuppressWarnings("WeakerAccess")
public interface SettingsStorage
{

    /**
     * This loads all Settings from the List.
     *
     * @param settings This is a List with all Settings that should be loaded.
     * @return true, if there was no problem while loading the Settings, otherwise false.
     */
    boolean load(List<SettingsProperty> settings);

    /**
     * This saves all Settings from the List.
     *
     * @param settings This is a List with all Settings that should be saved.
     * @return true, if there was no problem while saving the Settings, otherwise false.
     */
    boolean save(List<SettingsProperty> settings);

}
