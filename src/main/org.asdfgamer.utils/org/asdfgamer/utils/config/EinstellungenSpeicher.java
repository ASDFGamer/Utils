package org.asdfgamer.utils.config;

/**
 * Dieses Interface wird dafür verwendet, dass verschiedene Speichermöglichkeiten für die Einstellungen verwendet werden können.
 */
public interface EinstellungenSpeicher
{

    /**
     * Diese Methode wird dafür verwendet, dass einzelne Einstellungen zu Laden.
     *
     * @param name Dies ist der Name der Einstellung die geladen werden soll.
     * @return Dies ist der Wert der Einstellung
     */
    //public String getEinstellung(String name);

    /**
     * Diese Methode lädt alle Einstellungen aus einer Klasse und updated diese auch.
     *
     * @return Eine Map mit allen Einstellungen
     */
    public boolean getEinstellungen(IEinstellungen einstellungen);

    /**
     * Mit dieser Methode können alle Einstellungen einer Klasse gespeichert werden.
     *
     * @param einstellungen Dies ist die Klasse in der die Einstellungen vorhanden sind.
     * @return true, falls das speichern der Einstellungen erfolgreich war, ansonsten false.
     */
    public boolean speichern(IEinstellungen einstellungen);

}
