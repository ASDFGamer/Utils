package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertiesSpeicher implements EinstellungenSpeicher
{

    private static final Logger LOG = Logger.getLogger(PropertiesSpeicher.class.getName());

    private final String PROGRAMM_NAME;

    private String pfad = null;

    public PropertiesSpeicher(String name)
    {

        PROGRAMM_NAME = name;
    }

    public PropertiesSpeicher(String name, String pfad)
    {

        PROGRAMM_NAME = name;
        this.pfad = pfad;
    }

//    /**
//     * Diese Methode wird dafür verwendet, dass einzelne Einstellungen zu Laden.
//     *
//     * @param name Dies ist der Name der Einstellung die geladen werden soll.
//     * @return Dies ist der Wert der Einstellung
//     */
//    @Override
//    public String getEinstellung(String name)
//    {
//
//        return null;
//    }

    /**
     * Diese Methode lädt alle Einstellungen aus der angegbenen Klasse und updated sie auch gleich..
     *
     * @return trud, falls es keinen Fehler beim Laden gab, ansonsten false.
     */
    @Override
    public boolean getEinstellungen(IEinstellungen einstellungenKlasse)
    {

        if (pfad == null)
        {
            pfad = Utils.getConfigFile(PROGRAMM_NAME, "config.txt");
        }
        boolean result = true;
        Properties properties = new Properties();
        InputStream configFile = null;
        boolean einstellungenNichtVollstaendig = false;//TODO damit was machen
        Map<String, EinstellungenProperty> einstellungenList = Utils.getFields(einstellungenKlasse);

        for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungenList.entrySet())
        {
            if (!einstellung.getValue().isInternerWert())
            {
                einstellung.getValue().removeListener(EinstellungenListener.getEinstellungenAendern(einstellung.getValue()));//TODO testen
            }
        }

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
                }
                einstellung.getValue().set(properties.getProperty(einstellung.getKey(), einstellung.getValue().getStandardwert()));
            }
        } catch (IOException e)
        {
            LOG.log(Level.WARNING, "Die Configfile konnte nicht geladen werden.", e);
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

        if (pfad == null)
        {
            pfad = Utils.getConfigFile(PROGRAMM_NAME, "config.txt");
        }
        Map<String, EinstellungenProperty> einstellungen = Utils.getFields(einstellungenKlasse);
//        LOG.info("PropertiesSpeicher.speichern Zeile 103");
//        LOG.info(einstellungen.size() + "");
//        LOG.info(einstellungen.get(1).getName());
        boolean result = true;
//        if (!Einstellungen.einstellungenGeaendert && !einstellungenNichtVollstaendig)
//        {
//            LOG.info("Es wurde keine Einstellung geändert, deshalb muss nichts gespeichert werden.");
//            return true;
//        }
        Properties properties = new Properties();
        OutputStream configFile = null;

        try
        {
            configFile = new FileOutputStream(pfad);
//            einstellungen.forEach();
//            for( int i = 0; i<einstellungen.size(); i++)
            for (Map.Entry<String, EinstellungenProperty> einstellung : einstellungen.entrySet())
            {
                if (!einstellung.getValue().isInternerWert())
                {
                    properties.setProperty(einstellung.getKey(), einstellung.getValue().get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), "Dies sind die Einstellungen des Spiels '" + PROGRAMM_NAME + "'.");
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

//    /**
//     * Diese Methode sucht aus der angegebenen Klasse alle Einstellungen heraus und speichert diese in einem Array.
//     *
//     * @param einstellungenListe Dies ist die Klasse in der die Einstellungen sind.
//     * @return Ein Array aus allen Einstellungen.
//     */
//    private EinstellungenProperty[] findeEinstellungen(IEinstellungen einstellungenListe) {
//        return new EinstellungenProperty[]{};//TODO fertigstellen oder auslagen
//    }
}
