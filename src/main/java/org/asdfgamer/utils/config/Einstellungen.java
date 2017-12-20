package org.asdfgamer.utils.config;

import org.asdfgamer.utils.other.Utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Erstellt und verwaltet EinstellungsPropertys und Einstellungsklassen.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Einstellungen
{

    /**
     * Der benutzte Logger.
     */
    private final static Logger LOG = getLogger(Einstellungen.class.getName());

    /**
     * Dies gibt an ob die Methode die ein Array aus Objekten annnimmt zum erstellen geneutzt wurde.
     */
    private static boolean objArrErsteller = false;

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
     *
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung()
    {

        EinstellungenProperty property = new EinstellungenProperty();
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        return property;
    }

    /**
     * Dies nimmt ein Array von Objekten mit unbestimmter länge entgegen und erstellt daraus eine Einstellung.
     * Durch die nicht vorhandene Typbindung fallen Fehler erst im Betrieb auf, aber dafür muss es z.B. in Enums nur einen Kontruktor geben.
     *
     * @param einstellungen Dies ist das Aray aus Objekten welches analysiert werden soll.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(Object[] einstellungen)
    {

        objArrErsteller = true;
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
                LOG.warning("Es wurden zu viele Argumente für die Einstellung angegeben: " + Arrays.toString(einstellungen));
                return new EinstellungenProperty();

        }
    }
//

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
//

    /**
     * Diese Methode parst Array mit zwei Objekten so, dass am Ende eine EinstellungsProperty erstellt wird.
     *
     * @param einstellungen Das Array mit den 2 Argumenten.
     * @return Eine EinstellungsProperty.
     */
    private static EinstellungenProperty zweiArgumente(Object[] einstellungen)
    {

        if (einstellungen.length < 2)
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
//

    /**
     * Diese Methode parst Array mit zwei Objekten so, dass am Ende eine EinstellungsProperty erstellt wird.
     *
     * @param einstellungen Das Array mit den 3 Argumenten.
     * @return Eine EinstellungsProperty.
     */
    private static EinstellungenProperty dreiArgumente(Object[] einstellungen)
    {

        if (einstellungen.length < 3)
        {
            LOG.warning("Die Methode erwartet Array mit drei Argumenten, diese hat aber weniger.");
            return new EinstellungenProperty();
        }

        if (einstellungen[0] instanceof Integer && einstellungen[1] instanceof Integer && einstellungen[2] instanceof Integer)
        {
            return neueEinstellung((Integer) einstellungen[0], (Integer) einstellungen[1], (Integer) einstellungen[2]);
        } else if (einstellungen[0] instanceof Double && einstellungen[1] instanceof Double && einstellungen[2] instanceof Double)
        {
            return neueEinstellung((Double) einstellungen[0], (Double) einstellungen[1], (Double) einstellungen[2]);
        }
        LOG.warning("Die Einstellung mit drei Argumenten ist von einem nicht unterstützten Typ.");
        return new EinstellungenProperty();
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem String als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(String standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(standardwert);
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);

        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(int standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem double als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(double standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setDouble(standardwert);
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);
        return property;
    }

    //Interne Einstelungen

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem boolschen Wahrheitswert als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(boolean standardwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setBoolean(standardwert);
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem String als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(String standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(standardwert, intern);
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(int standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Double als Standartwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(double standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        property.setDouble(standardwert);
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem boolschen Wahrheitswert als Standarwert.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param intern       Dies gibt an ob die Einstellung nur intern sein soll
     *                     oder auch in der Einstellungsdatei gespeichert werden
     *                     soll.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(boolean standardwert, boolean intern)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert), intern);
        property.setBoolean(standardwert);
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     * Außerdem wird der Bereich angegeben in dem die Einstellung liegen darf.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param minimalwert  Der niedrigste erlaubte Wert.
     * @param maximalwert  Der höchste erlaubte Wert.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(int standardwert, int minimalwert, int maximalwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setMinWert(minimalwert);
        property.setMaxWert(maximalwert);
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);
        return property;
    }

    /**
     * Hiermit wird eine Einstllungsproperty erstellt mit einem Integer als Standardwert.
     * Außerdem wird der Bereich angegeben in dem die Einstellung liegen darf.
     *
     * @param standardwert Der Standardwert der Einstellung.
     * @param minimalwert  Der niedrigste erlaubte Wert.
     * @param maximalwert  Der höchste erlaubte Wert.
     * @return Die neue Einstellung
     */
    public static EinstellungenProperty neueEinstellung(double standardwert, double minimalwert, double maximalwert)
    {

        EinstellungenProperty property = new EinstellungenProperty(String.valueOf(standardwert));
        property.setMinWert(minimalwert);
        property.setMaxWert(maximalwert);
        property.addListener(EinstellungenListener.getEinstellungenAendern(property));
        addKlasse(property);
        return property;
    }

    private static void addKlasse(EinstellungenProperty property)
    {

        String klassenname;
        if (objArrErsteller)
        {
            klassenname = Thread.currentThread().getStackTrace()[5].getClassName();
            objArrErsteller = false;
        } else
        {
            klassenname = Thread.currentThread().getStackTrace()[3].getClassName();
        }
        property.setKlasse(klassenname);
        EinstellungKlassenInfos.add(klassenname);
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus der angegebenen Klasse geladen.
     * Hierfür wird der angegebene Speicher verwendet.
     *
     * @param einstellungen Dies ist die Klasse in der die Einstellungen definiert sind.
     * @return true, falls das Laden der Einstellungen erfolgreich war, ansonsten false.
     */
    public boolean laden(Object einstellungen)
    {
//        List<EinstellungenProperty> properties = Utils.getFields(einstellungen);

        if (isEnum(einstellungen))
        {
            return SPEICHER.getEinstellungen(getEinstellungenFromEnum(einstellungen));
        }
        return SPEICHER.getEinstellungen(Utils.getFields(einstellungen));
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
     * Mit dieser Funktion werden alle Einstellungspropertys aus dem angegebenen Enum gefunden und zurückgegeben
     *
     * @param einstellungenEnum Das Class-Objekt zu dem Enum
     * @return Eine Map mit allen Einstellungen
     */
    private Map<String, EinstellungenProperty> getEinstellungenFromEnum(Object einstellungenEnum)
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
        return einstellungen;
    }

    /**
     * Hiermit werden alle Einstellungen aus allen Klassen geladen.
     * Hierfür wird der angegebene Speicher verwendet.
     * Um zu überprüfen ob alle Einstellungen einer bestimmten Klasse vollständig geladen wurden muss auf die
     * {@link EinstellungKlassenInfos} zugeriffen werden.
     *
     * @return true, falls Einstellungen alle geladen wurden, ansonsten false.
     */
    public boolean laden()
    {

        Set<String> klassen = EinstellungKlassenInfos.getKlassen();
        boolean ergebnis = false;
        for (String klasse : klassen)
        {
            try
            {
                if (isEnum(Class.forName(klasse).getClass()))
                {
                    ergebnis = SPEICHER.getEinstellungen(getEinstellungenFromEnum(Class.forName(klasse).getClass()));
                } else
                {
                    ergebnis = SPEICHER.getEinstellungen(Utils.getFields(Class.forName(klasse))) && ergebnis;
                }
            } catch (ClassNotFoundException e)
            {
                LOG.warning("Bei der Klasse " + klasse + "gab es ein Problem beim Wiederfinden der " +
                        "Klasse zum Speichern. Somit wird sie nun nicht gespeichert.");
                ergebnis = false;
            }
        }
        return ergebnis;
    }

    /**
     * Mit dieser Methode werden die Einstellungen aus der Klasse die angegeben wurde gespeichert.
     *
     * @param einstellungen Dies ist die KLasse in der die Einstellungen gespeichert sind.
     * @return true, falls das Speichern erfolgreich war, ansonsten false.
     */
    public boolean speichern(Object einstellungen)
    {

        if (isEnum(einstellungen))
        {
            return SPEICHER.speichern(getEinstellungenFromEnum(einstellungen));
        }
        return SPEICHER.speichern(Utils.getFields(einstellungen));
    }

    /**
     * Hiermit werden alle Einstellungen aus allen Klassen gespeichert.
     * Hierfür wird der angegebene Speicher verwendet.
     *
     * @return true, falls alle Einstellungen gespeichert wurden, ansonsten false.
     */
    public boolean speichern()
    {

        Set<String> klassen = EinstellungKlassenInfos.getKlassen();
        boolean ergebnis = true;
        for (String klasse : klassen)
        {
            try
            {
                if (isEnum(Class.forName(klasse).getClass()))
                {
                    ergebnis = SPEICHER.speichern(getEinstellungenFromEnum(Class.forName(klasse).getClass()));
                } else
                {
                    ergebnis = SPEICHER.speichern(Utils.getFields(Class.forName(klasse))) && ergebnis;
                }
            } catch (ClassNotFoundException e)
            {
                LOG.warning("Bei der Klasse " + klasse + "gab es ein Problem beim Wiederfinden der " +
                        "Klasse zum Speichern. Somit wird sie nun nicht gespeichert.");
                ergebnis = false;
            }

        }
        return ergebnis;
    }
}
