package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hiermit können Einstellungen in einer Properties-Datei gespeichert werden.
 * Diese Datei liegt dann in dem Betriebssystem-abhänigen Configordner. Unter Windows ist dies
 * %AppData%\\Roaming\\"PROGRAMM_NAME", unter Linux .config/"PROGRAMM_NAME" und unter anderen
 * Betriesbssystemen wird direkt ein Ordner im Benutzerverzeichnis erstellt.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PropertiesSpeicher implements EinstellungenSpeicher
{

    /**
     * Dies ist der benutzte Logger.
     */
    private static final Logger LOG = Logger.getLogger(PropertiesSpeicher.class.getName());

    /**
     * Dies ist die Standardmäßeige Dateiendungen falls keine Endung angegeben wurde.
     */
    private static final String STD_EXTENSION = ".properties";

    /**
     * Dies ist der Name des Programmes welches die Einstellugen speichert.
     */
    private final String PROGRAMM_NAME;

    private final boolean STD_PROPERTIES;

    /**
     * Dies gibt an, ob die Einstellungen nach änderungen untersucht werden sollen und nur bei änderungen abgespeichert
     * werden, oder ob immer alle Einstellungen gespeichert werden.
     */
    private boolean checkForChanges = false;

    /**
     * Hiermit wir ein neuer Einstellungsspeicher erzeugt für die Datei mit dem angegebenen Namen.
     *
     * @param progname Der Name des Programms
     */
    public PropertiesSpeicher(String progname)
    {

        this.PROGRAMM_NAME = progname;
        this.STD_PROPERTIES = false;
    }

    /**
     * Hiermit wir ein neuer Einstellungsspeicher erzeugt für die Datei mit dem angegebenen Namen.
     *
     * @param progname         Der Name des Programms
     * @param checkForChanges  Dies gibt an, ob die Einstellungen nur bei änderungen gespeichert werden sollen.
     *                         Standardmäßig false.
     * @param useStdProperties Dies gibt an, ob das Standardobjekt von Java zum Speichern der Properties verwendet
     *                         werden soll, oder die Eingeninterpretation, welche die Einstellungen sortiert und
     *                         Kommentare/Abschnittstitel schreiben kann.
     */
    public PropertiesSpeicher(String progname, boolean checkForChanges, boolean useStdProperties)
    {

        this.PROGRAMM_NAME = progname;
        this.checkForChanges = checkForChanges;
        this.STD_PROPERTIES = useStdProperties;
    }

    /**
     * Diese Methode lädt alle Einstellungen aus dem angegbenenen Klassenpfad.
     *
     * @param einstellungen Dies sind alle Einstellungen als Map
     * @return false, falls es einen Fehler beim Laden gab, ansonsten true..
     */
    @Override
    public boolean getEinstellungen(Map<String, EinstellungenProperty> einstellungen)
    {

        boolean ergebnis = true;
        Map<String, Map<String, EinstellungenProperty>> klassen = sortiereEinstellungenNachKlassen(einstellungen);
        for (Map.Entry<String, Map<String, EinstellungenProperty>> klassenEinstellungen : klassen.entrySet())
        {
            //Nur der Klassenname und nicht der volltändige
            String name = klassenEinstellungen.getKey().substring(klassenEinstellungen.getKey().lastIndexOf('.') + 1);
            String pfad = erstellePfad(name);

            ergebnis = ladeEinstellungenPropertys(klassenEinstellungen.getValue(), pfad) && ergebnis;

        }
        LOG.warning("PropertiesSpeicher.getSettings Zeile 90 !!!!!!!!!!!!!!!!!!!!!!!!!!" + ergebnis);
        return ergebnis;
    }

    /**
     * Mit dieser Methode können Einstellungen gespeichert werden.
     *
     * @param einstellungen Dies ist eine Map mit allen Einstellungen.
     * @return true, falls das speichern der Einstellungen erfolgreich war, ansonsten false.
     */
    @Override
    public boolean speichern(Map<String, EinstellungenProperty> einstellungen)
    {

        boolean ergebnis = true;
        Map<String, Map<String, EinstellungenProperty>> klassen = sortiereEinstellungenNachKlassen(einstellungen);
        for (Map.Entry<String, Map<String, EinstellungenProperty>> klassenEinstellungen : klassen.entrySet())
        {
            //Nur der Klassenname und nicht der volltändige
            String name = klassenEinstellungen.getKey().substring(klassenEinstellungen.getKey().lastIndexOf('.') + 1);
            String pfad = erstellePfad(name);

            ergebnis = speicherEinstellungenPropertys(klassenEinstellungen.getValue(), pfad) && ergebnis;

        }
        return ergebnis;

    }

    /**
     * Hiermit werden alle EinstellungsProperys aus der angegebenen Map abgespeichert.
     *
     * @param einstellungen Die Einstellungen die abgespeichert werden sollen.
     * @param pfad          Dies ist der Pfad zu der Einstellungsdatei die verwendet werden soll.
     * @return true, falls es keine Probleme beim speichern gab, ansonsten false.
     */
    private boolean speicherEinstellungenPropertys(Map<String, EinstellungenProperty> einstellungen, String pfad)
    {

        if (checkForChanges)
        {
            if (keineAenderungen(einstellungen))
            {
                return true;
            }
        }

        Properties properties = new Properties();
        OutputStream configFile = null;

        try
        {
            configFile = new FileOutputStream(pfad);
            for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
            {
                if (!einstellung.getValue().isInternerWert())
                {
                    LOG.info("speichern " + einstellung.getKey());
                    properties.setProperty(einstellung.getKey(), einstellung.getValue().get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), "Dies sind die Einstellungen des Spiels '" + PROGRAMM_NAME + "'.");
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Es gab Probleme beim Öffnen der Datei zum Speichern der Einstellungen", e);
            return false;
        } finally
        {
            if (configFile != null)
            {
                try
                {
                    configFile.close();
                } catch (IOException e)
                {
                    LOG.log(Level.SEVERE, "Die Configfile konnte nicht geschlossen werden.", e);
                }
            }
        }
        return true;
    }

    /**
     * Hiermit wird überprüft ob es in der gesamten Map keine Änderungen gibt.
     *
     * @param einstellungen Die Map mit den Einstellungen.
     * @return true, falls es keine Änderung gab, ansonsten false.
     */
    private boolean keineAenderungen(Map<String, EinstellungenProperty> einstellungen)
    {

        boolean einstellungGeaendert = false;

        for (Map.Entry<String, EinstellungenProperty> entry : einstellungen.entrySet())
        {
            if (entry.getValue().getEinstellungGeaendert())
            {
                einstellungGeaendert = true;
            }
        }

        if (!einstellungGeaendert)
        {
            LOG.info("Es wurde keine Einstellung geändert, deshalb muss nichts gespeichert werden.");
            return true;
        }
        return false;
    }

    /**
     * Hiermit werden die Einstellungen verschachtelt in eine Klassenstruktur. Hierbei ist in der ersten Map der Key der
     * Klassenname, und in der zweiten Map der Key der Einstellungsname.
     *
     * @param einstellungen Dies ist eine Map mit verschiedenen Einstellungen, die aus verschiedenen Klassen kommen(können)
     * @return Eine verschachtelte Map, bei der die erste den Klassennamen angibt, und die zweite den Einstellungsnamen.
     */
    private Map<String, Map<String, EinstellungenProperty>> sortiereEinstellungenNachKlassen(Map<String, EinstellungenProperty> einstellungen)
    {

        Map<String, Map<String, EinstellungenProperty>> klassenEinstellungen = new HashMap<>();
        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
        {
            if (klassenEinstellungen.containsKey(einstellung.getValue().getKlasse()))
            {
                klassenEinstellungen.get(einstellung.getValue().getKlasse()).put(einstellung.getKey(), einstellung.getValue());
            } else
            {
                Map<String, EinstellungenProperty> klasse = new HashMap<>();
                klasse.put(einstellung.getKey(), einstellung.getValue());
                klassenEinstellungen.put(einstellung.getValue().getKlasse(), klasse);
            }
        }
        return klassenEinstellungen;
    }

    /**
     * Diese Methode üerprüft ob ein Pfad existiert und versucht ansonstenen einen zu erstellen.
     * Falls kein Programmname angegeben ist kann kein Pfad erzeugt werden und desshalb wird false zurückgegeben.
     * Ansonsten wird ein neuer Pfad aus den bekannten Infos erstellt und true zurückgegeben
     *
     * @param dateiName Dies ist der Name der Datei die überprüft werden soll.
     * @return Der Pfad zu der Configdatei.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private String erstellePfad(String dateiName)
    {

        return Utils.getConfigFile(PROGRAMM_NAME, dateiName + STD_EXTENSION);
    }

    /**
     * Hiermit werden die Einstellugenpropertys einer Klasse geladen. Es soll hier nur eine KLasse zur Zeit übergeben werden.
     *
     * @param einstellungen Die Einstellungspropertys
     * @param pfad          Der PFad zu der Datei
     * @return true, falls das Laden der Einstellungen erfolgreich war.
     */
    private boolean ladeEinstellungenPropertys(Map<String, EinstellungenProperty> einstellungen, String pfad)
    {

        boolean result = true;
        Properties properties = new Properties();
        InputStream configFile = null;

        //Change Listener entfernen
        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
        {
            if (!einstellung.getValue().isInternerWert())
            {
                einstellung.getValue().removeListener(EinstellungenListener.getEinstellungenAendern(einstellung.getValue()));
            }
        }
        //Laden der Einstellungen
        try
        {
            configFile = new FileInputStream(pfad);
            properties.load(configFile);

            for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
            {
                if (properties.getProperty(einstellung.getKey()) == null && !einstellung.getValue().isInternerWert())
                {
                    result = false;
                    LOG.warning("Für die Einstellung " + einstellung.getKey() + " konnte kein Wert geladen werden.");
                    SettingClassInfo.setProblemsWithLoading(einstellung.getValue().getKlasse());
                }
                einstellung.getValue().set(properties.getProperty(einstellung.getKey(), einstellung.getValue().getStandardwert()));
                SettingClassInfo.setSettingsLoaded(einstellung.getValue().getKlasse());//Wir zu häufig aufgerufen.
            }
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Die Configfile konnte nicht geladen werden.", e);
            result = false;
        } finally
        {
            //Einstellungen schließen
            if (configFile != null)
            {
                try
                {
                    configFile.close();
                } catch (IOException e)
                {
                    LOG.log(Level.SEVERE, "Die Configfile konnte nicht geschlossen werden.", e);
                }
            }
        }

        //Change Listener wieder hinzufügen
        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
        {
            if (!einstellung.getValue().isInternerWert())
            {
                einstellung.getValue().addListener(EinstellungenListener.getEinstellungenAendern(einstellung.getValue()));
            }
        }

        return result;
    }

}
