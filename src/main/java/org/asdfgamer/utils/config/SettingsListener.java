package org.asdfgamer.utils.config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.asdfgamer.utils.config.internal.SettingUtils;

import java.util.Locale;

/**
 * This Class is used to store common Listeners for the Settings.
 *
 * @author ASDFGamer
 */
@SuppressWarnings("WeakerAccess")
class SettingsListener
{

    /**
     * This provides an Listener that notes it in the setting if the value changed.
     * This Listener is as standard in every Setting active.
     * @param setting The Setting that should get the Listener.
     * @return The new ChangeListener.
     */
    public static ChangeListener<String> getSettingChange(Setting setting)
    {

        return new ChangeListener<>()
        {

            /**
             * This method needs to be provided by an implementation of
             * {@code ChangeListener}. It is called if the value of an
             * {@link ObservableValue} changes.
             * <p>
             *
             * @param observable The {@code ObservableValue} which value changed
             * @param oldValue   The old value
             * @param newValue   The new value
             */
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {

                if (oldValue != null && newValue != null && !oldValue.equals(newValue))
                {
                    setting.setSettingChanged();
                }
            }
        };
    }

    /**
     * This returns the SettingsChange Listener.
     *
     * @return The SettingsChanged Listener.
     */
    public static ChangeListener<String> getLanguageChangeListener()
    {

        return (observable, oldValue, newValue) -> SettingUtils.setLocale(Locale.forLanguageTag(newValue));
    }
}
