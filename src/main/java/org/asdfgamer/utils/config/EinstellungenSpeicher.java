package org.asdfgamer.utils.config;

import java.util.Map;

/**
 * Dieses Interface wird dafür verwendet, dass verschiedene Speichermöglichkeiten für die Einstellungen verwendet werden können.
 *
 * TODO anpassen auf Java Module mit provides/uses
 *
 * @author ASDFGamer
 * @version 0.9
 */
public interface EinstellungenSpeicher
{

    /**
     * Diese Methode lädt alle Einstellungen aus einer Klasse und updated diese auch.
     *
     * @param einstellungen Dies ist eine Map die als Key den Namen der Einstellung hat und als Wert die zugehörige
     *                      EinstellungenProperty.
     * @return true, falls es kein Problem beim Laden gab, ansonsten false.
     */
    boolean getEinstellungen(Map<String, EinstellungenProperty> einstellungen);

    /**
     * Mit dieser Methode können alle Einstellungen einer Klasse gespeichert werden.
     *
     * @param einstellungen Dies ist eine Map die als Key den Namen der Einstellung hat und als Wert die zugehörige
     *                      EinstellungenProperty.
     * @return true, falls das speichern der Einstellungen erfolgreich war, ansonsten false.
     */
    boolean speichern(Map<String, EinstellungenProperty> einstellungen);

}
