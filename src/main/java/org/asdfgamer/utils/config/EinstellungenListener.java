package org.asdfgamer.utils.config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * In dieser Klasse sind verschiedene Listener die sinnvoll auf EinstellungenPropertys angewendet werden können.
 *
 * @author ASDFGamer
 */
public class EinstellungenListener
{

    public static ChangeListener<String> getEinstellungenAendern(EinstellungenProperty einstellung)
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
                    einstellung.setEinstellungenGeaendert();
                }
            }
        };
    }
}
