package org.asdfgamer.utils.config;

import javafx.beans.property.SimpleStringProperty;
import org.asdfgamer.utils.other.Convertible;
import org.asdfgamer.utils.other.Utils;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * This is the Property in which the setting gets saved.
 *
 * @author ASDFGamer
 * @version 0.9
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public class SettingsProperty extends SimpleStringProperty
{

    /**
     * The used Logger
     */
    private static final Logger LOG = Logger.getLogger(SettingsProperty.class.getName());

    /**
     * This are all values that can be interpreted as 'true' TODO add possibility to edit this values
     * This are the values true and the version in the used language (german:wahr)
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static String[] TRUE_VALUES = {"true"};

    /**
     * This are all values that can be interpreted as 'false'
     * This are the values false and the version in the used language (german:falsch)
     */
    @SuppressWarnings("SpellCheckingInspection")
    private static String[] FALSE_VALUES = {"false"};

    /**
     * This notes that something changed in some setting. TODO check if some way is implemented to see this for a class.
     */
    private static boolean somethingChanged = false;

    private static Locale locale = Locale.getDefault();

    private static final ResourceBundle bundle = ResourceBundle.getBundle("config/Settings", locale);

    /**
     * This is the default value for the Setting, interpreted as String.
     */
    private final String defaultValue;

    /**
     * This shows if this setting is only for internally use and shouldn't be saved, etc.
     */
    private final boolean internalValue;

    /**
     * This is the Value of the Setting as boolean, if existent. If it is not existent that this is null.
     */
    private Boolean valueBoolean = null;

    /**
     * This is the Value of the Setting as integer, if existent. If it is not existent that this is null.
     */
    private Integer valueInteger = null;

    /**
     * This is the Value of the Setting as double, if existent. If it is not existent that this is null.
     */
    private Double valueDouble = null;

    /**
     * This shows the highest allowed value. If it is null then there is no upper bound.
     */
    private Double maximum = null;

    /**
     * This shows the lowest allowed value. If it is null then there is no lower bound.
     */
    private Double minimum = null;

    /**
     * This shows if the value of this Setting changed.
     */
    private boolean changed = false;

    /**
     * This is the name of the class n which this setting is defined.
     */
    private String CLASS_NAME;

    /**
     * This is an short text that gives Information about the Setting
     */
    private String INFO_TEXT = "";

    private int lineNumber = 0;


    /**
     * This is the default constructor, everything uses default values.
     */
    public SettingsProperty()
    {

        super();
        this.defaultValue = null;
        internalValue = false;
    }

    /**
     * This Constructor uses a default/initial value, everything else uses default values.
     * If the value can be interpreted as an integer, double or boolean value, the setting will do so and accept only
     * values of that type afterwards.
     *
     * @param initialValue The default/initial value of the setting.
     */
    SettingsProperty(String initialValue)
    {

        super(initialValue);
        this.defaultValue = initialValue;
        this.init(initialValue);
        internalValue = false;
    }

    /**
     * This constructor receives the default/initial value and if this setting is internal, everything else uses
     * default values.
     *
     * @param initialValue  The default/initial value of the setting.
     * @param internalValue This shows if the Setting is only for internal use.
     */
    SettingsProperty(String initialValue, boolean internalValue)
    {

        super(initialValue);
        this.defaultValue = initialValue;
        this.init(initialValue);
        this.internalValue = internalValue;
    }

    /**
     * This shows if any Setting was changed (during the active time of the ChangeListener).
     * By default is this always the case only while loading is the Listener deactivated.
     * <p>
     * Important: This are all settings and not just the Settings of one class/file.
     *
     * @return true, if a setting changed, otherwise false.
     */
    public static boolean getAnySettingChanged()
    {

        return somethingChanged;
    }

    /**
     * This is needed, because to initialise the Setting that saves the language this needs to be initialised.
     *
     * @param newLocale The new locale
     */
    protected static void setLocale(Locale newLocale)
    {

        locale = newLocale;
        TRUE_VALUES = new String[]{TRUE_VALUES[0], bundle.getString("true_value")};
        FALSE_VALUES = new String[]{FALSE_VALUES[0], bundle.getString("false_value")};
    }

    /**
     * This Methode gets called from the Constructor to check if the initial value convertible to boolean, integer or
     * double. If so it saves the value in that format.
     *
     * @param initialValue The initial Value of the setting.
     */
    private void init(String initialValue)
    {

        if (Convertible.toBoolean(initialValue, TRUE_VALUES, FALSE_VALUES))
        {
            this.valueBoolean = Utils.isTrue(initialValue, TRUE_VALUES);
        } else if (Convertible.toInt(initialValue))
        {
            this.valueInteger = Integer.parseInt(initialValue);
        } else if (Convertible.toDouble(initialValue))
        {
            this.valueDouble = Double.parseDouble(initialValue);
        }
    }

    /**
     * This shows if a Setting is only for internal use. This means for example that it shouldn't be saved.
     *
     * @return true, if it is only an internal value, otherwise false.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInternalValue()
    {

        return internalValue;
    }

    /**
     * This returns the default value.
     *
     * @return The default value.
     */
    public String getDefaultValue()
    {

        return defaultValue;
    }

    /**
     * This sets the boolean value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as boolean.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean setBoolean(boolean newValue)
    {

        if (this.valueBoolean != null)
        {
            this.valueBoolean = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning(bundle.getString("noBooleanValue"));
            return false;
        }

    }

    /**
     * This returns the boolean value of the setting.
     *
     * @return The boolean value of the Setting or 'null' if the setting has no boolean value.
     */
    public Boolean getBoolean()
    {

        return this.valueBoolean;
    }

    /**
     * This returns the integer value of the setting.
     *
     * @return The integer value of the Setting or 'null' if the setting has no integer value.
     */
    public Integer getInteger()
    {

        return this.valueInteger;
    }

    /**
     * This returns the double value of the setting. If there is no double value it returns the integer value.
     * If there is also no integer value then it returns 'null'.
     *
     * @return This returns the double value of the setting. If there is no double value it returns the integer value.
     * If there is also no integer value then it returns 'null'.
     */
    public Double getDouble()
    {

        if (this.valueDouble != null)
        {
            return this.valueDouble;
        } else
        {
            if (this.valueInteger != null)
            {
                return this.valueInteger.doubleValue();
            } else
            {
                return null;
            }
        }
    }

    /**
     * This shows if the setting has an boolean value.
     *
     * @return true, if there is an boolean value, otherwise false.
     */
    public boolean hasBooleanValue()
    {

        return this.valueBoolean != null;
    }

    /**
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * The methode {@link SettingsProperty#setString(String)} does exactly the same thing, but returns an boolean value
     * instead of the exception.
     *
     * @param newValue The new value that should be assigned.
     * @throws IllegalArgumentException This happens if the setting has for example an integer value and
     *                                  the new value is a 'pure' string or boolean or double.
     */
    @Override
    public void set(String newValue) throws IllegalArgumentException
    {

        if (this.valueBoolean != null)
        {
            if (Convertible.toBoolean(newValue, TRUE_VALUES, FALSE_VALUES))
            {
                this.valueBoolean = Utils.isTrue(newValue, TRUE_VALUES);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_boolean"));
            }
        }

        if (this.valueInteger != null)
        {
            if (Convertible.toInt(newValue))
            {
                setInteger(Integer.parseInt(newValue));
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_integer"));
            }
        }

        if (this.valueDouble != null)
        {
            if (Convertible.toDouble(newValue))
            {
                setDouble(Double.valueOf(newValue));
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_double"));
            }
        }

        super.set(newValue);
    }

    /**
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * This is exactly the same as the {@link SettingsProperty#set(String)} methode but if that methode throws an
     * exception this methode returns false.
     *
     * @param newValue The new value that should be assigned.
     * @return false, if the setting has for example an integer value an the new value is a 'pure' string or boolean
     * or double.
     */
    public boolean setString(String newValue)
    {

        try
        {
            set(newValue);
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    /**
     * This sets the integer value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as a integer.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean setInteger(int newValue)
    {

        if (this.valueInteger != null)
        {
            if (this.maximum != null && this.maximum < newValue)
            {
                newValue = this.maximum.intValue();
            } else if (this.minimum != null && this.minimum > newValue)
            {
                newValue = this.minimum.intValue();
            }
            this.valueInteger = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning(bundle.getString("noIntegerValue"));
            return false;
        }
    }

    /**
     * This sets the double value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as double
     * (and not as integer).
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean setDouble(double newValue)
    {

        if (this.valueDouble != null)
        {
            if (this.maximum != null && this.maximum < newValue)
            {
                newValue = this.maximum;
            } else if (this.minimum != null && this.minimum > newValue)
            {
                newValue = this.minimum;
            }
            this.valueDouble = newValue;
            super.set(String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning(bundle.getString("noDoubleValue"));
            return false;
        }
    }

    /**
     * This sets a maximum for the integer or double value. If this setting should get an higher value the value will
     * get set to ths value.
     *
     * @param maximum The highest possible value for this setting.
     * @return true, if the value was set, false if the setting can't have an maximum, because it is a boolean or string.
     */
    public boolean setMaximumValue(double maximum)
    {

        if (hasDoubleValue())
        {
            this.maximum = maximum;
        } else if (hasIntegerValue())
        {
            this.maximum = maximum;
            LOG.fine(bundle.getString("DoubleValueToInteger"));
        } else
        {
            LOG.fine(bundle.getString("noMaximumValueAllowed"));
            return false;
        }

        return true;
    }

    /**
     * This returns if the Setting has an Double-value.
     * If the setting has only an integer value this returns false.
     *
     * @return true, falls a double-value exists, otherwise false.
     */
    public boolean hasDoubleValue()
    {

        return this.valueDouble != null;
    }

    /**
     * This returns if the Setting has an Integer-value.
     *
     * @return true, if a integer-value exists, otherwise false.
     */
    public boolean hasIntegerValue()
    {

        return this.valueInteger != null;
    }

    /**
     * This sets a maximum for the integer value. If this setting should get an higher value the value will
     * get set to ths value.
     *
     * @param maximum The highest possible value for this setting.
     * @return true, if the value was set, false if the setting can't have an maximum, because it is a boolean or string.
     */
    public boolean setMaximumValue(int maximum)
    {

        if (hasDoubleValue() || hasIntegerValue())
        {
            this.maximum = (double) maximum;
        } else
        {
            LOG.fine(bundle.getString("noMaximumValueAllowed"));
            return false;
        }
        return true;
    }

    /**
     * This sets a minimum for the integer or double value. If this setting should get an lower value the value will
     * get set to ths value.
     *
     * @param minimum The lowest possible value for this setting.
     * @return true, if the value was set, false if the setting can't have an minimum, because it is a boolean or string.
     */
    public boolean setMinimumValue(double minimum)
    {

        if (hasDoubleValue())
        {
            this.minimum = minimum;
        } else if (hasIntegerValue())
        {
            this.minimum = minimum;
            LOG.fine(bundle.getString("DoubleValueToInteger"));
        } else
        {
            LOG.fine(bundle.getString("noMinimumValueAllowed"));
            return false;
        }
        return true;
    }

    /**
     * This sets a minimum for the integer or double value. If this setting should get an lower value the value will
     * get set to ths value.
     *
     * @param minimum The lowest possible value for this setting.
     * @return true, if the value was set, false if the setting can't have an minimum, because it is a boolean or string.
     */
    public boolean setMinimumValue(int minimum)
    {

        if (hasDoubleValue() || hasIntegerValue())
        {
            this.minimum = (double) minimum;
        } else
        {
            LOG.fine(bundle.getString("noMinimumValueAllowed"));
            return false;
        }
        return true;
    }

    /**
     * This returns the maximum for this setting, if one exists.
     *
     * @return The maximum of this setting or null if it doesn't exists.
     */
    public Double getMaximum()
    {

        return maximum;
    }

    /**
     * This returns the minimum for this setting, if one exists.
     *
     * @return The minimum of this setting or null if it doesn't exists.
     */
    public Double getMinimum()
    {

        return minimum;
    }

    /**
     * This sets that this setting was changed. Moreover it sets that any setting was changed. This can't be reversed.
     */
    protected void setSettingChanged()
    {

        changed = true;
        somethingChanged = true;
    }

    /**
     * This shows if this Setting was changed, while the ChangeListener was active.
     * By default it is always active, except when this setting gets loaded.
     *
     * @return true, if this Setting changed, otherwise false.
     */
    public boolean getSettingChanged()
    {

        return changed;
    }

    /**
     * This returns the Classname of this Property.
     *
     * @return the Classname of the class that created this property.
     */
    String getClassName()
    {

        return CLASS_NAME;
    }

    /**
     * This is used to set the name of the class that declared this Setting. For example {@link SettingsConfig} and not
     * {@link Settings}.
     *
     * @param className This is the complete name of the defining class.
     * @return true, if the name wasn't already set, otherwise false
     */
    protected boolean setClassName(String className)
    {

        if (this.CLASS_NAME == null)
        {
            this.CLASS_NAME = className;
            return true;
        }
        return false;
    }

    /**
     * This is used to set the information text for this setting. This can only be set one time.
     *
     * @param text Information about this setting
     * @return true, if there wasn't any text set, otherwise false.
     */
    protected boolean setInformationText(String text)
    {

        if (this.INFO_TEXT.isEmpty())
        {
            this.INFO_TEXT = text;
            return true;
        }
        return false;
    }

    /**
     * This returns the information text or null if it isn't set.
     *
     * @return the information text or "" if it isn't set.
     */
    public String getInformationText()
    {

        return INFO_TEXT;
    }

    /**
     * This gets the Line number of the setting in the class.
     *
     * @return The Line number in the class or '0' if it can't get the number.
     */
    public int getLineNumber()
    {

        return lineNumber;
    }

    /**
     * This sets the Line Number of the property in the class where it is defined.
     *
     * @param lineNumber The Line number.
     */
    protected void setLine(int lineNumber)
    {

        this.lineNumber = lineNumber;
    }
}