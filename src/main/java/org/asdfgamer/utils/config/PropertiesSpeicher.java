package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.io.*;
import java.lang.reflect.Method;
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
     * Diese Methode lädt alle Einstellungen aus der angegbenen Klasse und updated sie auch gleich.
     *
     * @return false, falls es einen Fehler beim Laden gab, ansonsten true.
     */
    @Override
    public boolean getEinstellungen(Object einstellungenKlasse)
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
    public boolean speichern(Object einstellungenKlasse)
    {

        if (!objectPasst(einstellungenKlasse))
        {
            return false;
        }

        if (!pfadExists())
        {
            return false;
        }

        if (isEnum(einstellungenKlasse))
        {
            return speicherEnum(einstellungenKlasse);
        } else
        {
            return speicherEinstellungenPropertys(Utils.getFields(einstellungenKlasse));
        }

    }

    /**
     * Diese Methode üerprüft ob ein Pfad existiert und versucht ansonstenen einen zu erstellen.
     * Falls kein Programmname angegeben ist kann kein Pfad erzeugt werden und desshalb wird false zurückgegeben.
     * Ansonsten wird ein neuer Pfad aus den bekannten Infos erstellt und true zurückgegeben
     *
     * @return Falls kein Pfad existert oder erstellt werden kann: false, ansonsten true.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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

    /**
     * Hiermit werden alle EinstellungsProperys aus der angegebenen Map abgespeichert.
     *
     * @param einstellungen Die Einstellungen die abgespeichert werden sollen.
     * @return true, falls es keine Probleme beim speichern gab, ansonsten false.
     */
    private boolean speicherEinstellungenPropertys(Map<String, EinstellungenProperty> einstellungen)
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
                    properties.setProperty(einstellung.getKey(), einstellung.getValue().get());
                }
            }

            properties.store(new OutputStreamWriter(configFile, "UTF-8"), "Dies sind die Einstellungen des Spiels '" + DATEI_NAME + "'.");
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
     * Dies verarbeitet ein Object bei dem es sich das Class-Object eines Enums handeln muss so, dass alle
     * EinstellungenProperys des Enums angespeichert werden.
     *
     * @param einstellungenEnum Das Class-Objekt des Enums.
     * @return true, falls das speichern geklappt hat, ansonsten false
     */
    private boolean speicherEnum(Object einstellungenEnum)
    {
        String enumName = ((Class) einstellungenEnum).getName();
        Map<String, EinstellungenProperty> einstellungen = new HashMap<>();
        try
        {
            Object[] enumConstants = Class.forName(enumName).getEnumConstants();
            for (Object enumConstant : enumConstants)
            {
                if (enumConstant instanceof IEinstellungen)
                {
                    Method[] methods = enumConstant.getClass().getDeclaredMethods();
                    for (Method method : methods)
                    {
                        if (method.getReturnType().equals(EinstellungenProperty.class))
                        {
                            try
                            {
                                einstellungen.putIfAbsent(((Enum) enumConstant).name(), (EinstellungenProperty) method.invoke(enumConstant));
                            } catch (ReflectiveOperationException e)
                            {
                                LOG.warning("Es gab ein Problem beim abrufen einer Einstellung aus der Funktion: " + method.getName() + " \n Dies tritt z.B. auf, falls argumente gefordert sind.");
                            }
                        }
                    }
                }
            }
        } catch (ClassNotFoundException e)
        {
            LOG.severe("Es gab ein Problem beim Wiederfinden der Klasse \"" + ((Class) einstellungenEnum).getName() + "\".");
            e.printStackTrace();
        }
        return speicherEinstellungenPropertys(einstellungen);
    }

    /**
     * Hiermit wird überprüft ob es sich bei dem übergebenen Objekt um ein Class-Objekt eines Enums handelt.
     *
     * @param einstellungenKlasse Das Objekt das überprüft werden soll.
     * @return true, falls es ein Ennum ist, ansonsten false.
     */
    private boolean isEnum(Object einstellungenKlasse)
    {
        return (einstellungenKlasse instanceof Class) && (((Class) einstellungenKlasse).isEnum());
    }

    /**
     * Hiermit wird überprüft ob es sich bei dem Objekt entweder um eine Instanz von IEinstellungen oder ein Enum handelt.
     * Es wird genauer gesagt geprüft ob es sich um das Class-Objekt des Enums handelt.
     *
     * @param einstellungenKlasse Das Objekt das überprüft werden soll.
     * @return true, falls es sich um einen weiter zu verarbeitetnen Typ handelt.
     */
    private boolean objectPasst(Object einstellungenKlasse)
    {
        if ((!(einstellungenKlasse instanceof IEinstellungen)) && (!isEnum(einstellungenKlasse)))
        {
            LOG.warning("Die angegebene Klasse muss entweder IEinstellungen implementieren oder ein Enum sein.");
            return false;
        }
        return true;
    }

}
