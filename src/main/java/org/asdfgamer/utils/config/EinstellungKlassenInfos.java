package org.asdfgamer.utils.config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * In dieser Klasse sind verschiedene Infos hinterlegt die für eine gesamte Klasse/Enum mit Einstellungen gelten und
 * deshalb nicht in den Einstellungproperties gespeichert werden können.
 *
 * TODO diese Klasse sinnvoll nutzen. Hierfür muss ich die Klasse in der eine Einstellung erstellt wurde herausfinden.
 */
public class EinstellungKlassenInfos
{

    private final static Logger LOG = getLogger(EinstellungKlassenInfos.class.getName());

    /**
     * Dies sind die Infos die zu einer Klasse gepeichert werden.
     */
    private class KlassenInfos
    {

        /**
         * Dies gibt an, ob eine EInstellng in dieser Klasse abgeändert wurde.
         */
        private boolean geaendert = false;

        /**
         * Dies gibt an, wie viele EInstellungen in dieser Klasse existieren.
         */
        private int einstellungen = 0;

    }

    /**
     * Dies ist die Map in der zu allen relevanten Klassen die Infos hinterlegt sind.
     */
    private Map<String, KlassenInfos> infos = new HashMap<>();

    /**
     * Hiermit wird angegeben, dass die angegebene Klasse abgeändert wurde.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     */
    public void setChanged(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            infos.get(klasse).geaendert = true;
        } else
        {
            KlassenInfos klassenInfos = new KlassenInfos();
            klassenInfos.geaendert = true;
            infos.put(klasse, klassenInfos);
        }
    }

    /**
     * Dies speichert wie viele Einstellungen in der Klasse vorhanden sind. Diese Anzahl kann sich nicht ändern.
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     * @param einstellungen Dies ist die Anzahl der EInstellungen
     * @return true, falls der Anzahl gesetzt werden konnte und false, falls die Anzahl schon gesetzt war
     */
    public boolean setEinstellungsanzahl(String klasse, int einstellungen)
    {

        if (infos.containsKey(klasse))
        {
            if (infos.get(klasse).einstellungen != 0)
            {
                LOG.warning("Die anzahl der Einstellungen wurde schon gesetzt und darf nicht mehr abgeändert werden.");
                return false;
            }
            infos.get(klasse).einstellungen = einstellungen;

        } else
        {
            KlassenInfos klassenInfos = new KlassenInfos();
            klassenInfos.einstellungen= einstellungen;
            infos.put(klasse, klassenInfos);
        }
        return true;
    }

    /**
     * Dies gibt an, ob die angegebene Klasse abgeändert wurde.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     * @return true, falls eine Einstellung geändert wurde und false, falls keine geändert wurde oder zu der Klasse
     * keine Infos existieren
     */
    public boolean isChanged(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            return infos.get(klasse).geaendert;
        }
        LOG.warning("Zu der Klasse: " + klasse + "wurden noch keine Infos hinterlegt.");
        return false;
    }

    /**
     * Dies gibt an, wie viele Einstellungen in der angegbenen Klasse vorhanden sind.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     * @return Die anzahl der Einstellungen oder 0, falls zu der Klasse keine Infos existieren.
     */
    public int getEinstellungen(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            return infos.get(klasse).einstellungen;
        }
        LOG.warning("Zu der Klasse: " + klasse + "wurden noch keine Infos hinterlegt.");
        return 0;
    }
}
