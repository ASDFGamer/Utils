package org.asdfgamer.utils.config;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Erstellt und verwaltet EinstellungsPropertys und Einstellungsklassen.
 *
 * @author Christoph
 */
public class Einstellungen
{

    /**
     * Der benutzte Logger.
     */
    private final static Logger LOG = getLogger(Einstellungen.class.getName());

    /**
     * Der Name des Programms das die Einstellungen erstellt.
     */
    private final String NAME;

    /**
     * Der verwendete Einstellungsspeicher.
     */
    private final EinstellungenSpeicher SPEICHER;

    /**
     * Hiermit wird ein neues Einstellungsobjekt erzeugt welches zum Laden und speichern der Einstellungen benutzt werden kann.
     * Als Speicher wird an dieser Stelle eine Properties Datei gewählt.
     *
     * @param programmname Der Name des Programms das die Einstellungen erzeugt.
     */
    public Einstellungen(String programmname)
    {

        this.NAME = programmname;
        this.SPEICHER = new PropertiesSpeicher(programmname);
    }

    /**
     * Hiermit wird ein neues Einstellungsobjekt erzeugt welches zum Laden und speichern der Einstellungen beutzt werden kann.
     *
     * @param programmname Der Name des Programms das die Einstellungen erzeugt.
     * @param speicher     Die Speichermethode, die die Einstellungen persistent speichert.
     */
    public Einstellungen(String programmname, EinstellungenSpeicher speicher)
    {

        this.NAME = programmname;
        this.SPEICHER = speicher;
    }

    /**
     * Hiermit wird eine einfache Einstellungsproperty erstellt, falls keine zusätzlichen Infos angegeben werden.
     */
    public static EinstellungenProperty neueEinstellung()
    {

        EinstellungenProperty property = new EinstellungenProperty();
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
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
    public static EinstellungenProperty neueEinstellung(double standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        property.setDouble(standardwert);
        return property;
    }

    /**
     * Dies nimmt ein Array von Objekten mit unbestimmter länge entgegen und erstellt daraus eine Einstellung.
     * Durch die nicht vorhandene Typbindung fallen Fehler erst im Betrieb auf, aber dafür muss es z.B. in Enums nur einen Kontruktor geben.
     *
     * @param einstellungen
     */
    public static EinstellungenProperty neueEinstellung(Object[] einstellungen)
    {

        switch (einstellungen.length)
        {
            case 0:
                return new EinstellungenProperty();
            case 1:
                return einArgument(einstellungen[0]);
            case 2:
                return zweiArgumente(einstellungen);
            case 3:
                return dreiArgumente(einstellungen);
            default:
                LOG.warning("Es wurden zu viele Argumente für die Einstellung angegeben: " + einstellungen.toString());
                return new EinstellungenProperty();

        }
    }

    /**
     * Diese Methode parst Array mit zwei Objekten so, dass am Ende eine EinstellungsProperty erstellt wird.
     * @param einstellungen Das Array mit den 3 Argumenten.
     * @return Eine EinstellungsProperty.
     */
    private static EinstellungenProperty dreiArgumente(Object[] einstellungen)
    {
        if (einstellungen.length<3)
        {
            LOG.warning("Die Methode erwartet Array mit drei Argumenten, diese hat aber weniger.");
            return new EinstellungenProperty();
        }

        if (einstellungen[0] instanceof Integer && einstellungen[1] instanceof Integer && einstellungen[2] instanceof Integer)
        {
            return neueEinstellung((Integer)einstellungen[0],(Integer)einstellungen[1],(Integer)einstellungen[2]);
        }
        else if (einstellungen[0] instanceof Double && einstellungen[1] instanceof Double&& einstellungen[2] instanceof Double)
        {
            return neueEinstellung((Double) einstellungen[0],(Double) einstellungen[1],(Double) einstellungen[2]);
        }
        LOG.warning("Die Einstellung mit drei Argumenten ist von einem nicht unterstützten Typ.");
        return new EinstellungenProperty();
    }

    /**
     * Diese Methode parst Array mit zwei Objekten so, dass am Ende eine EinstellungsProperty erstellt wird.
     * @param einstellungen Das Array mit den 2 Argumenten.
     * @return Eine EinstellungsProperty.
     */
    private static EinstellungenProperty zweiArgumente(Object[] einstellungen)
    {
        if (einstellungen.length<2)
        {
            LOG.warning("Die Methode erwartet Array mit zwei Argumenten, diese hat aber weniger.");
            return new EinstellungenProperty();
        }

        if (einstellungen[0] instanceof String && einstellungen[1] instanceof Boolean)
        {
            return neueEinstellung((String) einstellungen[0], (Boolean) einstellungen[1]);
        } else if (einstellungen[0] instanceof Integer && einstellungen[1] instanceof Boolean)
        {
            return neueEinstellung((Integer) einstellungen[0], (Boolean) einstellungen[1]);
        } else if (einstellungen[0] instanceof Double && einstellungen[1] instanceof Boolean)
        {
            return neueEinstellung((Double) einstellungen[0], (Boolean) einstellungen[1]);
        } else if (einstellungen[0] instanceof Boolean && einstellungen[1] instanceof Boolean)
        {
            return neueEinstellung((Boolean) einstellungen[0], (Boolean) einstellungen[1]);
        }
        LOG.warning("Die Einstellung mit zwei Argumenten ist von einem nicht unterstützten Typ.");
        return new EinstellungenProperty();
    }

    /**
     * Hiermit wird ein Objekt umgewandelt in eine Einstellungsproperty die zu dem Typ des Argumentes passt.
     *
     * @param einstellung Das Argument für die Einstellung als String,Integer, Double oder Boolean.
     * @return Eine Einstellungsproperty.
     */
    public static EinstellungenProperty einArgument(Object einstellung)
    {


        if (einstellung instanceof String)
        {
            return neueEinstellung((String) einstellung);
        } else if (einstellung instanceof Integer)
        {
            return neueEinstellung((Integer) einstellung);
        } else if (einstellung instanceof Double)
        {
            return neueEinstellung((Double) einstellung);
        } else if (einstellung instanceof Boolean)
        {
            return neueEinstellung((Boolean) einstellung);
        }
        LOG.warning("Die Einstellung mit einem Argument ist von einem nicht unterstützten Typ.");
        return new EinstellungenProperty();
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus dem angegebenen Speicher geladen.
     *
     * @param einstellungen Dies ist die Klasse in der die Einstellungen definiert sind.
     * @return true, falls das Laden der Einstellungen erfolgreich war, ansonsten false.
     */
    public boolean lade(IEinstellungen einstellungen)
    {
//        List<EinstellungenProperty> properties = Utils.getFields(einstellungen);

        return SPEICHER.getEinstellungen(einstellungen);
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus der Klasse die angegeben wurde gespeichert.
     *
     * @param einstellungen Dies ist die KLasse in der die Einstellungen gespeichert sind.
     * @return true, falls das Speichern erfolgreich war, ansonsten false.
     */
    public boolean speicher(IEinstellungen einstellungen)
    {

        return SPEICHER.speichern(einstellungen);
    }
}
