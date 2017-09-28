package config;

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

    public Einstellungen(String programmname)
    {

        this.NAME = programmname;
        this.SPEICHER = new PropertiesSpeicher(programmname);
    }

    public Einstellungen(String programmname, EinstellungenSpeicher speicher)
    {

        this.NAME = programmname;
        this.SPEICHER = speicher;
    }

    /**
     * Hiermit wird eine einfache Einstellungsproperty erstellt, falls keine zusätzlichen infos angegeben werden.
     */
    public static EinstellungenProperty neueEinstellung()
    {

        EinstellungenProperty property = new EinstellungenProperty();
        //property.addListener(einstellungenAendern);
        return property;
    }

    /**
     * Der Konstruktor für Einstellungen aus Strings.
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
     * Der Konstruktor für Einstellungen aus Integern.
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
     * Der Konstruktor für Einstellungen aus Integern.
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
     * Der Konstruktor für Einstellungen aus boolschen Wahrheitswerten.
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
     * Der Konstruktor für Einstellungen aus double Zahlen.
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
     * Der Konstruktor für Einstellungen aus double Zahlen.
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

    //Interne Konstruktoren

    /**
     * Der Konstruktor für Einstellungen aus Strings.
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
     * Der Konstruktor für Einstellungen aus Integern.
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
     * Der Konstruktor für Einstellungen aus boolschen Wahrheitswerten.
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
     * Der Konstruktor für Einstellungen aus double Zahlen.
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

    public boolean lade(IEinstellungen einstellungen)
    {
//        List<EinstellungenProperty> properties = Utils.getFields(einstellungen);

        return SPEICHER.getEinstellungen(einstellungen);
    }

    public boolean speicher(IEinstellungen einstellungen)
    {

        return SPEICHER.speichern(einstellungen);
    }
}
