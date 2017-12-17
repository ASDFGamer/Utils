package org.asdfgamer.utils.config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * In dieser Klasse sind verschiedene Infos hinterlegt die für eine gesamte Klasse/Enum mit Einstellungen gelten und
 * deshalb nicht in den Einstellungproperties gespeichert werden können.
 */
public class EinstellungKlassenInfos
{

    private final static Logger LOG = getLogger(EinstellungKlassenInfos.class.getName());

    /**
     * Dies ist die Map in der zu allen relevanten Klassen die Infos hinterlegt sind.
     */
    private static Map<String, KlassenInfos> infos = new HashMap<>();

    /**
     * Hiermit wird angegeben, dass die angegebene Klasse abgeändert wurde.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     */
    public static void setChanged(String klasse)
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
     *
     * @param klasse        Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     * @param einstellungen Dies ist die Anzahl der EInstellungen
     * @return true, falls der Anzahl gesetzt werden konnte und false, falls die Anzahl schon gesetzt war
     */
    public static boolean setEinstellungsanzahl(String klasse, int einstellungen)
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
            klassenInfos.einstellungen = einstellungen;
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
    public static boolean isChanged(String klasse)
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
    public static int getEinstellungen(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            return infos.get(klasse).einstellungen;
        }
        LOG.warning("Zu der Klasse: " + klasse + "wurden noch keine Infos hinterlegt.");
        return 0;
    }

    /**
     * Hiermit wird festgelegt, dass die Einstellungen dieser Klasse schon geladen wurden.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     */
    public static void setEinstellungenGeladen(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            infos.get(klasse).einstellungenGeladen = true;
        } else
        {
            KlassenInfos klassenInfos = new KlassenInfos();
            klassenInfos.einstellungenGeladen = true;
            infos.put(klasse, klassenInfos);
        }

    }

    /**
     * Hiermit wird festgelegt, dass es ein Problem beim Laden der Einstellungen gab.
     *
     * @param klasse Der absolute Klassenname (z.B. org.asdfgamer.utils.config.Einstellungen)
     */
    public static void setProblemBeimLaden(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            infos.get(klasse).einstellungenVollstaengigGeladen = false;
            infos.get(klasse).einstellungenGeladen = true;

        } else
        {
            KlassenInfos klassenInfos = new KlassenInfos();
            klassenInfos.einstellungenVollstaengigGeladen = false;
            klassenInfos.einstellungenGeladen = true;
            infos.put(klasse, klassenInfos);
        }
    }

    public static boolean getProblemBeimLaden(String klasse)
    {

        if (infos.containsKey(klasse))
        {
            if (infos.get(klasse).einstellungenGeladen)
            {
                return !infos.get(klasse).einstellungenVollstaengigGeladen;
            }
        }
        return false;
    }

    /**
     * Dies sind die Infos die zu einer Klasse gepeichert werden.
     */
    private static class KlassenInfos
    {

        /**
         * Dies gibt an, ob eine Einstellung in dieser Klasse abgeändert wurde.
         */
        private boolean geaendert = false;

        /**
         * Dies gibt an, wie viele Einstellungen in dieser Klasse existieren.
         */
        private int einstellungen = 0;

        /**
         * Dies gibt an, ob die Einstellungen dieser Klasse schon geladen wurden.
         */
        private boolean einstellungenGeladen = false;

        /**
         * Dies gibt an, ob die Einstellungen vollständig geladen wurden, als sie das letzte mal geladen wurden.
         * Falls {@link KlassenInfos#einstellungenGeladen}==false gilt, dann gibt dieses Feld keine sinnvollen Infos ab.
         */
        private boolean einstellungenVollstaengigGeladen = true;
    }
}
