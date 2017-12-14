package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hiermit können Einstellungen in einer Properties-Datei gespeichert werden.
 * Diese Datei liegt dann in dem Betriebssystem-abhänigen Configordner. Unter Windows ist dies
 * %AppData%\\Roaming\\"PROGRAMM_NAME", unter Linux .config/"PROGRAMM_NAME" und unter anderen
 * Betriesbssystemen wird direkt ein Ordner im Benutzerverzeichnis erstellt.
 */
public class PropertiesSpeicher implements EinstellungenSpeicher
{

    /**
     * Dies ist der benutzte Logger.
     */
    private static final Logger LOG = Logger.getLogger(PropertiesSpeicher.class.getName());

    /**
     * Dies ist die Standardmäßeige Dateiendungen falls keine Endung angegeben wurde.
     */
    private static final String STD_EXTENSION = ".cfg";

    /**
     * Dies ist der Name des Programmes welches die Einstellugen speichert.
     */
    private static String PROGRAMM_NAME = null;

    /**
     * Dies ist der Name der Datei in der die Einstellungen gespeichert werden sollen.
     */
    private final String DATEI_NAME;

    /**
     * Dies ist der Pfad zu der Datei, wird nur lokal berechnet
     */
    private String pfad = null;

    /**
     * Dies gibt an, ob die Einstellungen nach änderungen untersucht werden sollen und nur bei änderungen abgespeichert
     * werden, oder ob immer alle Einstellungen gespeichert werden.
     */
    private boolean checkForChanges = false;

    /**
     * Hiermit wird der Programmname gesetzt und somit der Ordner festgelegt in dem die Einstellungen gespeichert werden.
     *
     * @param programmname Dies ist der Programmname
     * @return true, falls das ändern geklappt hat, ansonsten false.
     */
    public static boolean setProgrammName(String programmname)
    {

        if (PROGRAMM_NAME != null)
        {
            LOG.warning("Der Programmname wurde schon gesetzt und darf nicht geändert werden.");
            return false;
        }
        PROGRAMM_NAME = programmname;
        return true;
    }

    /**
     * Hiermit wir ein neuer Einstellungsspeicher erzeugt für die Datei mit dem angegebenen Namen.
     *
     * @param name Der Name der Datei
     */
    public PropertiesSpeicher(String name)
    {

        DATEI_NAME = name;
    }


    /**
     * Hiermit wir ein neuer Einstellungsspeicher erzeugt für die Datei mit dem angegebenen Namen.
     *
     * @param name            Der Name der Datei
     * @param checkForChanges Dies gibt an, ob die Einstellungen nur bei änderungen gespeichert werden sollen.
     *                        Standardmäßig false.
     */
    public PropertiesSpeicher(String name, boolean checkForChanges)
    {

        DATEI_NAME = name;
        this.checkForChanges = checkForChanges;
    }

    /**
     * Diese Methode lädt alle Einstellungen aus der angegbenen Klasse und updated sie auch gleich.
     *
     * @return false, falls es einen Fehler beim Laden gab, ansonsten true.
     */
    @Override
    public boolean getEinstellungen(IEinstellungen einstellungenKlasse)
    {

        if (!pfadExists())
        {
            return false;
        }

        boolean result = true;
        Properties properties = new Properties();
        InputStream configFile = null;
        boolean einstellungenNichtVollstaendig = false;//TODO damit was machen, Dies gibt an, dass nicht alle einstellungen geladen werden konnten
        //Einstellungen finden
        Map<String, EinstellungenProperty> einstellungenList = Utils.getFields(einstellungenKlasse);

        //Change Listener entfernen
        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungenList.entrySet())
        {
            if (!einstellung.getValue().isInternerWert())
            {
                einstellung.getValue().removeListener(EinstellungenListener.getEinstellungenAendern(einstellung.getValue()));//TODO testen
            }
        }
        //Laden der Einstellungen
        try
        {
            configFile = new FileInputStream(pfad);
            properties.load(configFile);

            for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungenList.entrySet())
            {
                LOG.info("PropertiesSpeicher.getEinstellungen Zeile 79\n" + einstellung.getKey() + "\n" + properties.getProperty(einstellung.getKey()));

                if (properties.getProperty(einstellung.getKey()) == null && !einstellung.getValue().isInternerWert())
                {
                    einstellungenNichtVollstaendig = true;
                    LOG.warning("Für die Einstellung " + einstellung.getKey() + " konnte kein Wert geladen werden.");
                }
                einstellung.getValue().set(properties.getProperty(einstellung.getKey(), einstellung.getValue().getStandardwert()));
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
        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungenList.entrySet())
        {
            if (!einstellung.getValue().isInternerWert())
            {
                einstellung.getValue().addListener(EinstellungenListener.getEinstellungenAendern(einstellung.getValue()));
            }
        }

        return result;
    }


    /**
     * Mit dieser Methode können alle Einstellungen einer Klasse gespeichert werden.
     *
     * @param einstellungenKlasse Dies ist die Klasse in der die Einstellungen vorhanden sind.
     * @return true, falls das speichern der Einstellungen erfolgreich war, ansonsten false.
     */
    @Override
    public boolean speichern(IEinstellungen einstellungenKlasse)
    {

        if (!pfadExists())
        {
            return false;
        }

        Map<String, EinstellungenProperty> einstellungen = Utils.getFields(einstellungenKlasse);

        boolean result = true;

        if (checkForChanges)
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
        }

        Properties properties = new Properties();
        OutputStream configFile = null;

        if (!Utils.isFile(pfad))
        {
            if (!Utils.createFile(pfad))
            {
                LOG.severe("Es konnte keine Configdatei in dem Ordner " + pfad + " erzeugt werden.");
                return false;
            }
        }

        try
        {
            configFile = new FileOutputStream(pfad);
            for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
            {
                if (!einstellung.getValue().isInternerWert())
                {
                    properties.setProperty(einstellung.getKey(), einstellung.getValue().get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), "Dies sind die Einstellungen des Spiels '" + DATEI_NAME + "'.");
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Es gab Probleme beim Öffnen der Datei zum Speichern der Einstellungen", e);
            result = false;
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
        return result;
    }

    /**
     * Diese Methode üerprüft ob ein Pfad existiert und versucht ansonstenen einen zu erstellen.
     * Falls kein Programmname angegeben ist kann kein Pfad erzeugt werden und desshalb wird false zurückgegeben.
     * Ansonsten wird ein neuer Pfad aus den bekannten Infos erstellt und true zurückgegeben
     *
     * @return Falls kein Pfad existert oder erstellt werden kann: false, ansonsten true.
     */
    private boolean pfadExists()
    {

        if (pfad == null)
        {
            if (PROGRAMM_NAME == null)
            {
                LOG.severe("Es ist kein Programmname festgelegt.");
                return false;
            }
            if (DATEI_NAME.contains("."))
            {
                pfad = Utils.getConfigFile(PROGRAMM_NAME, DATEI_NAME);
            } else
            {
                pfad = Utils.getConfigFile(PROGRAMM_NAME, DATEI_NAME + STD_EXTENSION);
            }
        }
        return true;
    }

}
