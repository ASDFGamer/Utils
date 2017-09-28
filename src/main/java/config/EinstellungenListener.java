package config;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * In dieser Klasse sind verschiedene Listener die sinnvoll auf EinstellungenPropertys angewendet werden können.
 */
public class EinstellungenListener
{

    public static ChangeListener<String> getEinstellungenAendern(EinstellungenProperty einstellung)
    {

        ChangeListener<String> einstellungenAendern = new ChangeListener<String>()
        {

            /**
             * This method needs to be provided by an implementation of
             * {@code ChangeListener}. It is called if the value of an
             * {@link ObservableValue} changes.
             * <p>
             * In general is is considered bad practice to modify the observed value in
             * this method.
             *
             * @param observable The {@code ObservableValue} which value changed
             * @param oldValue   The old value
             * @param newValue
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
        return einstellungenAendern;
    }

//    /**
//     * Dies ist der Listender der die Variable 'einstellungenGeaendert' auf true
//     * setzt, falls sich Einstellungen geändert haben. Dies wird dafür
//     * gebraucht, dass nur neu abgespeichert wird falls sich etwas geändert hat.
//     */
//    public final ChangeListener<String> einstellungenAendern = new ChangeListener<String>()
//    {
//        /**
//         * This method needs to be provided by an implementation of
//         * {@code ChangeListener}. It is called if the value of an
//         * {@link ObservableValue} changes.
//         * <p>
//         * In general is is considered bad practice to modify the observed value in
//         * this method.
//         *
//         * @param observable The {@code ObservableValue} which value changed
//         * @param oldValue   The old value
//         * @param newValue
//         */
//        @Override
//        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//            if (oldValue != null && newValue != null && !oldValue.equals(newValue))
//            {
////                oldValue.setEinstellungenGeaendert();
//            }
//        }
//
//    };
}
