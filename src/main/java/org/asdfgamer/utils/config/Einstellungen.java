package org.asdfgamer.utils.config;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Dies sind alle Einstellungen die existieren.
 *
 * @author Christoph
 */
public class Einstellungen
{

    /**
     * Der Logger den wir benutzen.
     */
    private final static Logger LOG = getLogger(Einstellungen.class.getName());

    private final String NAME;

    private final EinstellungenSpeicher SPEICHER;

    /**
     * Hiermit wird ein neues Einstellungsobjekt erzeugt welches zum Laden und speichern der Einstellungen benutzt werden kann.
     * Als Speicher wird an dieser Stelle eine Properties Datei gewählt.
     *
     * @param programmname Der Name des Programms das die Einstellungen erzeugt.
     */
    public Einstellungen(String programmname) {

        this.NAME = programmname;
        this.SPEICHER = new PropertiesSpeicher(programmname);
    }

    /**
     * Hiermit wird ein neues Einstellungsobjekt erzeugt welches zum Laden und speichern der Einstellungen beutzt werden kann.
     * @param programmname Der Name des Programms das die Einstellungen erzeugt.
     * @param speicher Die Speichermethode, die die Einstellungen persistent speichert.
     */
    public Einstellungen(String programmname, EinstellungenSpeicher speicher) {

        this.NAME = programmname;
        this.SPEICHER = speicher;
    }

    /**
     * Hiermit wird eine einfache Einstellungsproperty erstellt, falls keine zusätzlichen Infos angegeben werden.
     */
    public static EinstellungenProperty neueEinstellung()
    {

        EinstellungenProperty property = new EinstellungenProperty();
        //property.addListener(einstellungenAendern);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem String als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     */
    public static EinstellungenProperty neueEinstellung(String standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(standardwert);
//        property.addListener(einstellungenAendern);
        return property;
    }
//

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     */
    public static EinstellungenProperty neueEinstellung(int standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
//        this.property.addListener(einstellungenAendern);
        return property;
    }
//

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     * Außerdem wird der Bereich angegeben in dem die Einstellung liegen darf.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param minimalwert  Der niedrigste erlaubte Wert.
     * @param maximalwert  Der höchste erlaubte Wert.
     */
    public static EinstellungenProperty neueEinstellung(int standardwert, int minimalwert, int maximalwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setMinWert(minimalwert);
        property.setMaxWert(maximalwert);
//        property.addListener(einstellungenAendern);
        return property;
    }
//

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem boolschen Wahrheitswert als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     */
    public static EinstellungenProperty neueEinstellung(boolean standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setBoolean(standardwert);
//        property.addListener(einstellungenAendern);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem double als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     */
    public static EinstellungenProperty neueEinstellung(double standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setDouble(standardwert);
//        property.addListener(einstellungenAendern);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     * Außerdem wird der Bereich angegeben in dem die Einstellung liegen darf.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param minimalwert  Der niedrigste erlaubte Wert.
     * @param maximalwert  Der höchste erlaubte Wert.
     */
    public static EinstellungenProperty neueEinstellung(double standardwert, double minimalwert, double maximalwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setMinWert(minimalwert);
        property.setMaxWert(maximalwert);
//        this.property.addListener(einstellungenAendern);
        return property;
    }

    //Interne Einstelungen

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem String als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     */
    public static EinstellungenProperty neueEinstellung(String standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(standardwert, intern);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     */
    public static EinstellungenProperty neueEinstellung(int standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem boolschen Wahrheitswert als Standarwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     */
    public static EinstellungenProperty neueEinstellung(boolean standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        property.setBoolean(standardwert);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Double als Standartwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     */
    public static EinstellungenProperty neueEinstellung(double standardwert, boolean intern) {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        property.setDouble(standardwert);
        return property;
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus dem angegebenen Speicher geladen.
     * @param einstellungen Dies ist die Klasse in der die Einstellungen definiert sind.
     * @return true, falls das Laden der Einstellungen erfolgreich war, ansonsten false.
     */
    public boolean lade(IEinstellungen einstellungen) {
//        List<EinstellungenProperty> properties = Utils.getFields(einstellungen);

        return SPEICHER.getEinstellungen(einstellungen);
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus der Klasse die angegeben wurde gespeichert.
     * @param einstellungen Dies ist die KLasse in der die Einstellungen gespeichert sind.
     * @return true, falls das Speichern erfolgreich war, ansonsten false.
     */
    public boolean speicher(IEinstellungen einstellungen) {

        return SPEICHER.speichern(einstellungen);
    }
}
