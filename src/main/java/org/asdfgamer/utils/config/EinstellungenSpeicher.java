package org.asdfgamer.utils.config;

/**
 * Dieses Interface wird dafür verwendet, dass verschiedene Speichermöglichkeiten für die Einstellungen verwendet werden können.
 *
 * @author ASDFGamer
 */
public interface EinstellungenSpeicher
{
    /**
     * Diese Methode lädt alle Einstellungen aus einer Klasse und updated diese auch.
     *
     * @param einstellungen Dies ist das Objekt von dem die Einstellungen gespeichert werden sollen (z.Z. IEinstellungen und Enum)
     * @return Eine Map mit allen Einstellungen
     */
    boolean getEinstellungen(Object einstellungen);

    /**
     * Mit dieser Methode können alle Einstellungen einer Klasse gespeichert werden.
     *
     * @param einstellungen Dies ist die Klasse in der die Einstellungen vorhanden sind.
     * @return true, falls das speichern der Einstellungen erfolgreich war, ansonsten false.
     */
    boolean speichern(Object einstellungen);

}
