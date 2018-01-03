package org.asdfgamer.utils.config;

import javafx.beans.property.SimpleStringProperty;
import org.asdfgamer.utils.other.Convertible;
import org.asdfgamer.utils.other.Utils;

import java.util.logging.Logger;

/**
 * Dies ist das Objekt in dem Einstellungen gespeichert werden.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public class EinstellungenProperty extends SimpleStringProperty
{

    /**
     * Dies sind alle Werte die zu 'true' umgewandelt werden können.
     */
    private static final String[] TRUE_VALUES = {"true", "wahr"};

    /**
     * Dies sind alle Werte die zu 'false' umgewandelt werden können.
     */
    private static final String[] FALSE_VALUES = {"false", "falsch"};

    /**
     * Der benutzte Logger
     */
    private static final Logger LOG = Logger.getLogger(EinstellungenProperty.class.getName());

    /**
     * Dies gibt an, dass irgendeine Einstellung geändert wurde. TODO anpassen, so dass es auf eine Klasse beschränkt wird. Hierfür muss die Klasse in der die EInstellung erstellt wird bekannt sein.
     */
    private static boolean irgendwasGeaendert = false;

    /**
     * Dies ist der Standardwert der Einstellung als String.
     */
    private final String STANDARDWERT;


    /**
     * Dies gitb an, ob diese Einstellung nur für den internen gebrauch gedacht ist und desshalb z.B. beim speichern ausgelassen wird.
     */
    private final boolean internerWert;

    /**
     * Dies gibt den Wert der Einstellung als boolean an, falls dieser existiert.
     */
    private Boolean wertBoolean = null;

    /**
     * Dies gibt den Wert der Einstellung als int an, falls dieser exitiert.
     */
    private Integer wertInteger = null;

    /**
     * Dies gibt den Wert der Einstellung als double an, falls dieser exitiert.
     */
    private Double wertDouble = null;

    /**
     * Dies gibt an was der höchste erlaubte Wert der Einstellung ist.
     */
    private Double maximalwert = null;

    /**
     * Dies gibt an was der niedrigste erlaubte Wert der Einstellung ist.
     */
    private Double minimalwert = null;

    /**
     * Dies gibt an, dass diese Einstellung geändert wurde.
     */
    private boolean geaendert = false;

    /**
     * Dies ist die Klasse in der die Property definiert ist.
     */
    private String KLASSE;

    /**
     * Diesem Konstruktor wurde kein Wert übergeben, alles wird mit Standardwerten aufgefüllt.
     */
    public EinstellungenProperty()
    {

        super();
        this.STANDARDWERT = null;
        internerWert = false;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert und der Initialwert übergeben, alles andere wird mit Standardwerten aufgefüllt.
     *
     * @param initialValue Der Anfangs-/ Standardwert der Einstellung.
     */
    EinstellungenProperty(String initialValue)
    {

        super(initialValue);
        this.STANDARDWERT = initialValue;
        this.init(initialValue);
        internerWert = false;
    }

    /**
     * Diese Methode wird vom Konstruktor aufgerufen und überprüft, ob der Anfangswert mehr ist als nur ein String.
     *
     * @param initialValue Der Anfangswert der Einstellung
     * @return true, falls es kein Problem gab, ansonsten false.
     */
    private boolean init(String initialValue)
    {

        if (Convertible.toBoolean(initialValue, TRUE_VALUES, FALSE_VALUES))
        {
            this.wertBoolean = Utils.isTrue(initialValue, TRUE_VALUES);
        } else if (Convertible.toInt(initialValue))
        {
            this.wertInteger = Integer.parseInt(initialValue);
        } else if (Convertible.toDouble(initialValue))
        {
            this.wertDouble = Double.parseDouble(initialValue);
        }
        return true;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert, der Name und die Bean übergeben, alles andere wird mit Standardwerten aufgefüllt.
     *
     * @param bean Die benutzte Bean.
     * @param name Der Name des Objekts.
     */
    EinstellungenProperty(Object bean, String name)
    {

        super(bean, name);
        internerWert = false;
        this.STANDARDWERT = null;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert, der Name, die Bean und der Anfangswert übergeben.
     *
     * @param bean         Die benutzte Bean.
     * @param name         Der Name des Objekts.
     * @param initialValue Der Anfangs-/ Standardwert der Einstellung.
     */
    EinstellungenProperty(Object bean, String name, String initialValue)
    {

        super(bean, name, initialValue);
        this.STANDARDWERT = initialValue;
        this.init(initialValue);
        internerWert = false;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert und der Initialwert übergeben, alles andere wird mit Standardwerten aufgefüllt.
     *
     * @param initialValue Der Anfangs-/ Standardwert der Einstellung.
     * @param internerWert Dies gibt an, ob die Einstellung nur intern gespeichert werden soll.
     */
    EinstellungenProperty(String initialValue, boolean internerWert)
    {

        super(initialValue);
        this.STANDARDWERT = initialValue;
        this.init(initialValue);
        this.internerWert = internerWert;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert, der Name und die Bean übergeben, alles andere wird mit Standardwerten aufgefüllt.
     *
     * @param bean         Die benutzte Bean.
     * @param name         Der Name des Objekts.
     * @param internerWert Dies gibt an, ob die Einstellung nur intern gespeichert werden soll.
     */
    EinstellungenProperty(Object bean, String name, boolean internerWert)
    {

        super(bean, name);
        this.internerWert = internerWert;
        this.STANDARDWERT = null;
    }

    /**
     * Diesem Konstruktor wurde der Standardwert, der Name, die Bean und der Anfangswert übergeben.
     *
     * @param bean         Die benutzte Bean.
     * @param name         Der Name des Objekts.
     * @param initialValue Der Anfangs-/ Standardwert der Einstellung.
     * @param internerWert Dies gibt an, ob die Einstellung nur intern gespeichert werden soll.
     */
    EinstellungenProperty(Object bean, String name, String initialValue, boolean internerWert)
    {

        super(bean, name, initialValue);
        this.STANDARDWERT = initialValue;
        this.init(initialValue);
        this.internerWert = internerWert;
    }

    /**
     * Dies gibt an, ob irgendeine Einstellung schon geändert wurde, während der ChangeListener aktiv war.
     * Standardmäßig ist diesr immer aktiv, nur z.B. beim Laden von Einstellungen wird dieser kurzzeitig deaktiviert.
     * <p>
     * Hierbei ist zu beachten, dass alle Einstellungen berücksichtigt werden und nicht nur die aus einer Datei.
     *
     * @return true, falls dieser Einstellung geändert wurde, ansonsten false.
     */
    public static boolean getEineEinstellungGeaendert()
    {

        return irgendwasGeaendert;
    }

    /**
     * Dies gibt an, ob diese Einstellung nur für den internen Gebrauch gedacht ist und nicht gespeichert werden soll.
     *
     * @return true, falls es nur ein interner Wert ist, ansonsten false.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInternerWert()
    {

        return internerWert;
    }

    /**
     * Dies gibt den Standardwert zurück.
     *
     * @return Der Standardwert.
     */
    public String getStandardwert()
    {

        return STANDARDWERT;
    }

    /**
     * Dies setzt den Booleanwert der Einstellung und updatet den Stringwert.
     *
     * @param newValue Der neue Wert.
     * @return true, falls alles gut gegangen ist, sonst false.
     */
    public boolean setBoolean(boolean newValue)
    {

        if (this.wertBoolean != null)
        {
            this.wertBoolean = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning("Diese Einstellung hat keinen Booleanwert");
            return false;
        }

    }

    /**
     * Dies gibt den booleanwert der Einstellung zurück
     *
     * @return Der booleanwert der Einstellung oder null falls sie nicht gesetzt wurde.
     */
    public Boolean getBoolean()
    {

        return this.wertBoolean;
    }

    /**
     * Dies gibt den Integerwert der Einstellung zurück
     *
     * @return Der Integerwert der Einstellung oder null falls sie nicht gesetzt wurde.
     */
    public Integer getInteger()
    {

        return this.wertInteger;
    }

    /**
     * Dies gibt den Doublewert der Einstellung zurück oder falls dieser nicht gesetzt wurde den Integerwert oder falls nichts gesetzt wurde null.
     *
     * @return Der Doublewert der Einstellung oder den Integerwert oder null falls nichts gesetzt wurde.
     */
    public Double getDouble()
    {

        if (this.wertDouble != null)
        {
            return this.wertDouble;
        } else
        {
            if (this.wertInteger != null)
            {
                return this.wertInteger.doubleValue();
            } else
            {
                return null;
            }
        }
    }

    /**
     * Dies gibt zurück ob die Einstellung einen booleanwert hat.
     *
     * @return true, falls ein booleanwert existert, sonst false.
     */
    public boolean hasBooleanValue()
    {

        return this.wertBoolean != null;
    }

    /**
     * Diese Methode gibt den Stringwert der Property zurück.
     *
     * @return Der Wert der Property als String.
     */
    @Override
    public String get()
    {

        return super.get();
    }

    /**
     * Hiermit wird der neue Wert festgelegt und falls notwendig auch der boolean, integer oder double Wert abgeändert.
     *
     * @param newValue Der neue Wert der zugewiesen werden soll.
     */
    @Override
    public void set(String newValue) throws IllegalArgumentException
    {

        if (this.wertBoolean != null)
        {
            if (Convertible.toBoolean(newValue, TRUE_VALUES, FALSE_VALUES))
            {
                this.wertBoolean = Utils.isTrue(newValue, TRUE_VALUES);
            } else
            {
                throw new IllegalArgumentException("Der Wert " + newValue + " konnte nicht zu boolean umgewandelt werden, obwohl die Einstellung vom Typ boolean ist.");
            }
        }

        if (this.wertInteger != null)
        {
            if (Convertible.toInt(newValue))
            {
                setInteger(Integer.parseInt(newValue));
            } else
            {
                throw new IllegalArgumentException("Der Wert " + newValue + " konnte nicht zu Integer umgewandelt werden, obwohl die Einstellung vom Typ Integer ist.");
            }
        }

        if (this.wertDouble != null)
        {
            if (Convertible.toDouble(newValue))
            {
                setDouble(Double.valueOf(newValue));
            } else
            {
                throw new IllegalArgumentException("Der Wert " + newValue + " konnte nicht zu Double umgewandelt werden, obwohl die Einstellung vom Typ Double ist.");
            }
        }

        super.set(newValue);
    }

    /**
     * Dies setzt den Integerwert der Einstellung und updatet den Stringwert.
     *
     * @param newValue Der neue Wert.
     * @return true, falls alles gut gegangen ist, sonst false.
     */
    public boolean setInteger(int newValue)
    {

        if (this.wertInteger != null)
        {
            if (this.maximalwert != null && this.maximalwert < newValue)
            {
                newValue = this.maximalwert.intValue();
            } else if (this.minimalwert != null && this.minimalwert > newValue)
            {
                newValue = this.minimalwert.intValue();
            }
            this.wertInteger = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning("Diese Einstellung hat keinen Integerwert");
            return false;
        }
    }

    /**
     * Dies setzt den Doublewert der Einstellung und updatet den Stringwert.
     *
     * @param newValue Der neue Wert.
     * @return true, falls alles gut gegangen ist, sonst false.
     */
    public boolean setDouble(double newValue)
    {

        if (this.wertDouble != null)
        {
            if (this.maximalwert != null && this.maximalwert < newValue)
            {
                newValue = this.maximalwert;
            } else if (this.minimalwert != null && this.minimalwert > newValue)
            {
                newValue = this.minimalwert;
            }
            this.wertDouble = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning("Diese Einstellung hat keinen Doublewert");
            return false;
        }
    }

    /**
     * Diese Methode setzt einen maximalen Wert für die Einstellung was dafür sorgt, dass jeder Wert der höher ist automatisch zu diesem Wert wird.
     * Falls es sich bei der Einstellung nicht um eine Integer oder Double Einstellung handelt passiert nichts und es wird 'false' zurückgegeben.
     *
     * @param maxWert Der maximale Wert der Einstellung
     * @return true, wenn das setzten des Wertes geklappt hat, false falls nicht oder falls die Einstellunng keinen maximalwert haben kann.
     */
    public boolean setMaxWert(double maxWert)
    {

        if (hasDoubleValue())
        {
            this.maximalwert = maxWert;
        } else if (hasIntegerValue())
        {
            this.maximalwert = maxWert;
            LOG.fine("Da es sich bei der Einstellung um eine Integereinstellung handelt wird der Doublewert in ein Integerwert umgewandelt.");
        } else
        {
            LOG.fine("Dies ist eine Einstellung die keinen Maximalwert haben kann.");
            return false;
        }

        return true;
    }

    /**
     * Dies gibt zurück ob die Einstellung einen Doublewert hat.
     *
     * @return true, falls ein Doublewert existert, sonst false.
     */
    public boolean hasDoubleValue()
    {

        return this.wertDouble != null;
    }

    /**
     * Dies gibt zurück ob die Einstellung einen Integerwert hat.
     *
     * @return true, falls ein Intergerwert existert, sonst false.
     */
    public boolean hasIntegerValue()
    {

        return this.wertInteger != null;
    }

    /**
     * Diese Methode setzt einen maximalen Wert für die Einstellung was dafür sorgt, dass jeder Wert der höher ist automatisch zu diesem Wert wird.
     * Falls es sich bei der Einstellung nicht um eine Integer oder Double Einstellung handelt passiert nichts und es wird 'false' zurückgegeben.
     *
     * @param maxWert Der maximale Wert der Einstellung
     * @return true, wenn das Setzen des Wertes geklappt hat, false falls nicht oder falls die Einstellunng keinen maximalwert haben kann.
     */
    public boolean setMaxWert(int maxWert)
    {

        if (hasDoubleValue() || hasIntegerValue())
        {
            this.maximalwert = (double) maxWert;
        } else
        {
            LOG.fine("Dies ist eine Einstellung die keinen Maximalwert haben kann.");
            return false;
        }
        return true;
    }

    /**
     * Diese Methode setzt einen minimalen Wert für die Einstellung was dafür sorgt, dass jeder Wert der niedriger ist automatisch zu diesem Wert wird.
     * Falls es sich bei der Einstellung nicht um eine Integer oder Double Einstellung handelt passiert nichts und es wird 'false' zurückgegeben.
     *
     * @param minWert Der minimale Wert der Einstellung
     * @return true, wenn das setzten des Wertes geklappt hat, false falls nicht oder falls die Einstellunng keinen minimalwert haben kann.
     */
    public boolean setMinWert(double minWert)
    {

        if (hasDoubleValue())
        {
            this.minimalwert = minWert;
        } else if (hasIntegerValue())
        {
            this.minimalwert = minWert;
            LOG.fine("Da es sich bei der Einstellung um eine Integereinstellung handelt wird der Doublewert in ein Integerwert umgewandelt.");
        } else
        {
            LOG.fine("Dies ist eine Einstellung die keinen Minimalwert haben kann.");
            return false;
        }
        return true;
    }

    /**
     * Diese Methode setzt einen minimalen Wert für die Einstellung was dafür sorgt, dass jeder Wert der niedriger ist automatisch zu diesem Wert wird.
     * Falls es sich bei der Einstellung nicht um eine Integer oder Double Einstellung handelt passiert nichts und es wird 'false' zurückgegeben.
     *
     * @param minWert Der minimale Wert der Einstellung
     * @return true, wenn das setzten des Wertes geklappt hat, false falls nicht oder falls die Einstellunng keinen minimalwert haben kann.
     */
    public boolean setMinWert(int minWert)
    {

        if (hasDoubleValue() || hasIntegerValue())
        {
            this.minimalwert = (double) minWert;
        } else
        {
            LOG.fine("Dies ist eine Einstellung die keinen Minimalwert haben kann.");
            return false;
        }
        return true;
    }

    /**
     * Dies gibt den Maximalwert der Einstellung zurück falls einer existert. Falls keiner existert gibt es null zurück
     *
     * @return Der maximalwert der Einstellung oder null.
     */
    public Double getMaximalwert()
    {

        return maximalwert;
    }

    /**
     * Dies gibt den Minimalwert der Einstellung zurück falls einer existert. Falls keiner existert gibt es null zurück
     *
     * @return Der Minimalwert der Einstellung oder null.
     */
    public Double getMinimalwert()
    {

        return minimalwert;
    }

    /**
     * Diese Methode legt fest, dass diese Einstellunggeändert wurde und auch allgemein, dass eine Einstellung geändert wurde. Dies kann nicht rückgängig gemacht werden.
     */
    protected void setEinstellungenGeaendert()
    {

        geaendert = true;
        irgendwasGeaendert = true;
    }

    /**
     * Dies gibt an, ob diese Einstellung schon geändert wurde, während der ChangeListener aktiv war.
     * Standardmäßig ist dieser immer aktiv, nur z.B. beim Laden von Einstellungen wird dieser kurzzeitig deaktiviert.
     *
     * @return true, falls dieser Einstellung geändert wurde, ansonsten false.
     */
    public boolean getEinstellungGeaendert()
    {

        return geaendert;
    }

    /**
     * Hiermit wird der Klassenname der Klasse die diese Property erstellt zurückgegeben.
     *
     * @return der Klassenname der Klasse die diese Property erstellt
     */
    String getKlasse()
    {
        return KLASSE;
    }

    /**
     * Hiermit wird die Klasse angegeben in der diese Property erfragt wurde (z.B. nicht  {@link Einstellungen} sondern
     * {@link SettingsEnum}). Dies ist für interne benutzung gedacht.
     *
     * @param klasse Dies ist der vollständige Klassenname der definierenden Klasse
     * @return true, falls das ändern geklappt hat, ansonten false
     */
    boolean setKlasse(String klasse)
    {
        if (this.KLASSE == null)
        {
            this.KLASSE = klasse;
            return true;
        }
        return false;
    }
}