package org.asdfgamer.utils.config;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.asdfgamer.utils.config.internal.SettingsInformation;
import org.asdfgamer.utils.other.Convertible;
import org.asdfgamer.utils.other.Utils;

import java.util.ArrayList;
import java.util.logging.Logger;

import static org.asdfgamer.utils.config.internal.SettingUtils.*;

/**
 * This is the Property in which the setting gets saved.
 *
 * @author ASDFGamer
 * @version 0.9
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public class SettingsProperty implements WritableStringValue, ObservableStringValue
{

    /**
     * The used Logger
     */
    private static final Logger LOG = Logger.getLogger(SettingsProperty.class.getName());

    /**
     * This is the default value for the Setting, interpreted as String.
     */
    private final String defaultValue;

    /**
     * This shows if this setting is only for internally use and shouldn't be saved, etc.
     */
    private final boolean internalValue;

    /**
     * This contains all information about this Setting that isn't connected to the value.
     */
    private SettingsInformation info;

    private final StringProperty valueString;

    /**
     * This is the Value of the Setting as String. This is the List that has all Listeners.
     */
    private ObservableList<String> valuesString = FXCollections.observableList(new ArrayList<>());
    /**
     * This is the Value of the Setting as boolean, if existent. If it is not existent that this is null.
     */
    private ObservableList<Boolean> valuesBoolean = FXCollections.observableList(new ArrayList<>());

    /**
     * This is the Value of the Setting as integer, if existent. If it is not existent that this is null.
     */
    private ObservableList<Integer> valuesInteger = FXCollections.observableList(new ArrayList<>());

    /**
     * This is the Value of the Setting as double, if existent. If it is not existent that this is null.
     */
    private ObservableList<Double> valuesDouble = FXCollections.observableList(new ArrayList<>());

    /**
     * This is the Value of the Setting as Enum, if existent. If it is not existent that this is null.
     */
    private ObservableList<Enum> valuesEnum = FXCollections.observableList(new ArrayList<>());

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
     * This is the default constructor, everything uses default values.
     */
    public SettingsProperty()
    {
        this.defaultValue = null;
        this.internalValue = false;
        this.valueString = new SimpleStringProperty();
        this.valuesString.addListener(getvalueUpdater());
    }

    private ListChangeListener<String> getvalueUpdater()
    {
        return c ->
        {
            c.next();
            if (c.getFrom() == 0)
            {
                valueString.setValue(valuesString.get(0));
            }
        };
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
        this.valuesString.add(initialValue);
        this.defaultValue = initialValue;
        this.valueString = new SimpleStringProperty(initialValue);
        this.valuesString.addListener(getvalueUpdater());
        this.init(initialValue);
        this.internalValue = false;
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
        this.valuesString.add(initialValue);
        this.valueString = new SimpleStringProperty(initialValue);
        this.defaultValue = initialValue;
        this.valuesString.addListener(getvalueUpdater());
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

        return SettingsInformation.isAnySettingChanged();
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
     * Get the wrapped value. This must be identical to
     * the value returned from {@link #getValue()}.
     * <p>
     * This method exists only to align WritableObjectValue API with
     * {@link WritableBooleanValue} and subclasses of {@link WritableNumberValue}
     *
     * @return The current value
     */
    @Override
    public String get()
    {
        return valuesString.get(0);
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

        if (this.valuesBoolean.size() > 0)
        {
            if (Convertible.toBoolean(newValue, TRUE_VALUES, FALSE_VALUES))
            {
                setBoolean(Utils.isTrue(newValue, TRUE_VALUES));
                //this.valuesBoolean = Utils.isTrue(newValue, TRUE_VALUES);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_boolean"));
            }
        }

        if (this.valuesInteger.size() > 0)
        {
            if (Convertible.toInt(newValue))
            {
                setInteger(Integer.parseInt(newValue));
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_integer"));
            }
        }

        if (this.valuesDouble.size() > 0)
        {
            if (Convertible.toDouble(newValue))
            {
                setDouble(Double.valueOf(newValue));
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_double"));
            }
        }

        if (this.valuesEnum.size() > 0)
        {
            if (Utils.isEnum(newValue))
            {
                setEnum(Utils.getEnumElement(newValue));
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_enum"));
            }
        }

        valuesString.set(0, newValue);
    }

    /**
     * This sets the Enum value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as an Enum.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean set(Enum newValue)
    {

        return setEnum(newValue);
    }

    /**
     * This sets the boolean value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as boolean.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean set(Boolean newValue)
    {

        return setBoolean(newValue);
    }

    /**
     * This sets the integer value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as a integer.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean set(Integer newValue)
    {

        return setInteger(newValue);
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
    public boolean set(Double newValue)
    {

        return setDouble(newValue);
    }

    /**
     * This sets the Enum value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as an Enum.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean setEnum(Enum newValue)
    {

        if (this.valuesEnum.size() > 0)
        {
            this.valuesEnum.set(0, newValue);
            valuesString.set(0, String.valueOf(newValue.getDeclaringClass() + "." + newValue));
            return true;
        } else
        {
            LOG.warning(bundle.getString("noEnumValue"));
            return false;
        }
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

        if (this.valuesInteger.size() > 0)
        {
            if (this.maximum != null && this.maximum < newValue)
            {
                newValue = this.maximum.intValue();
            } else if (this.minimum != null && this.minimum > newValue)
            {
                newValue = this.minimum.intValue();
            }
            this.valuesInteger.set(0, newValue);
            this.valuesString.set(0, String.valueOf(newValue));
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

        if (this.valuesDouble.size() > 0)
        {
            if (this.maximum != null && this.maximum < newValue)
            {
                newValue = this.maximum;
            } else if (this.minimum != null && this.minimum > newValue)
            {
                newValue = this.minimum;
            }
            this.valuesDouble.set(0, newValue);
            this.valuesString.set(0, String.valueOf(newValue));
            return true;
        } else
        {
            LOG.warning(bundle.getString("noDoubleValue"));
            return false;
        }
    }

    @SuppressWarnings("ClassEscapesDefinedScope")
    public boolean setSettingsInformation(SettingsInformation info)
    {

        if (this.info == null)
        {
            this.info = info;
            return true;
        }
        return false;
    }

    /**
     * This returns the Classname of this Property.
     *
     * @return the Classname of the class that created this property.
     */
    public String getClassName()
    {

        return info.getClassName();
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
     * This sets the boolean value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as boolean.
     *
     * @param newValue The new value.
     * @return true, if the value changed, otherwise false.
     */
    public boolean setBoolean(boolean newValue)
    {

        if (this.valuesBoolean.size() > 0)
        {
            this.valuesBoolean.set(0, newValue);
            this.valuesString.set(0, String.valueOf(newValue));
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

        return this.valuesBoolean.get(0);
    }

    /**
     * This returns the integer value of the setting.
     *
     * @return The integer value of the Setting or 'null' if the setting has no integer value.
     */
    public Integer getInteger()
    {

        return this.valuesInteger.get(0);
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

        if (this.valuesDouble.get(0) != null)
        {
            return this.valuesDouble.get(0);
        } else
        {
            if (this.valuesInteger.get(0) != null)
            {
                return this.valuesInteger.get(0).doubleValue();
            } else
            {
                return null;
            }
        }
    }

    /**
     * This returns the class of the Enum Value, if one is set.
     *
     * @return The class of the Enum Value, if one is set.
     */
    public Class<? extends Enum> getEnumType()
    {

        if (valuesEnum != null)
        {
            return valuesEnum.get(0).getClass();
        }
        return null;
    }

    /**
     * This return the Type of the Setting.
     *
     * @return The Type of the Setting.
     */
    public SettingsPropertyTypes getType()
    {

        if (hasBooleanValue())
        {
            return SettingsPropertyTypes.Boolean;
        } else if (hasDoubleValue())
        {
            return SettingsPropertyTypes.Double;
        } else if (hasIntegerValue())
        {
            return SettingsPropertyTypes.Integer;
        } else if (hasEnumValue())
        {
            return SettingsPropertyTypes.Enum;
        } else
        {
            return SettingsPropertyTypes.String;
        }
    }

    /**
     * This shows if the setting has an boolean value.
     *
     * @return true, if there is an boolean value, otherwise false.
     */
    public boolean hasBooleanValue()
    {

        return this.valuesBoolean.size() > 0;
    }

    /**
     * This returns if the Setting has an Double-value.
     * If the setting has only an integer value this returns false.
     *
     * @return true, falls a double-value exists, otherwise false.
     */
    public boolean hasDoubleValue()
    {

        return this.valuesDouble.size() > 0;
    }

    /**
     * This returns if the Setting has an Integer-value.
     *
     * @return true, if a integer-value exists, otherwise false.
     */
    public boolean hasIntegerValue()
    {

        return this.valuesInteger.size() > 0;
    }

    /**
     * This shows if the value of the Setting is an Enum.
     *
     * @return true, if this is an Enum, otherwise false.
     */
    public boolean hasEnumValue()
    {

        return this.valuesEnum.size() > 0;
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
     * This returns the information text or null if it isn't set.
     *
     * @return the information text or "" if it isn't set.
     */
    public String getInformationText()
    {

        return info.getInformationText();
    }

    /**
     * This gets the Line number of the setting in the class.
     *
     * @return The Line number in the class or '0' if it can't get the number.
     */
    public int getLineNumber()
    {

        return info.getLineNumber();
    }

    /**
     * This returns the name of the Setting.
     *
     * @return The name of the Setting.
     */
    public String getSettingName()
    {

        if (info.getSettingName() == null || info.getSettingName().isEmpty())
        {
            info.setSettingsName(this);
        }
        return info.getSettingName();
    }

    /**
     * This resets the setting to the default value.
     */
    public void setToDefaultValue()
    {

        set(getDefaultValue());
    }

    /**
     * This return the Value of the Setting as Enum value. If this Setting has no Enum value this returns null.
     *
     * @return The value at the index
     */
    public Enum getEnum()
    {

        return this.valuesEnum.get(0);
        //return getEnum(0);
    }

    /**
     * This shows if this setting is an List with more than one element or just a setting with one value.
     *
     * @return true, if the Setting is an list, otherwise false.
     */
    public boolean isList()
    {

        return getLength() > 1;
    }

    /**
     * This Methode returns the Setting as String. If there are more entries, then the first entry is returned.
     *
     * @return The value of the Setting as String
     */
    public String getString()
    {

        return get();
    }

    /**
     * This return the Value of the Setting at the given index as String. The first index is 0.
     * If there is no value at that index it returns null
     *
     * @param index The index of the Setting
     * @return The value at the index
     */
    public String getString(int index)
    {

        throw new UnsupportedOperationException();
    }

    /**
     * This return the Value of the Setting at the given index as Boolean. The first index is 0.
     * If there is no value at that index or it isn't boolean this returns null
     *
     * @param index The index of the Setting
     * @return The value at the index
     */
    public Boolean getBoolean(int index)
    {

        throw new UnsupportedOperationException();
    }

    /**
     * This return the Value of the Setting at the given index as Double. The first index is 0.
     * If there is no value at that index or it isn't a double this returns null
     *
     * @param index The index of the Setting
     * @return The value at the index
     */
    public Double getDouble(int index)
    {

        throw new UnsupportedOperationException();
    }

    /**
     * This return the Value of the Setting at the given index as Integer. The first index is 0.
     * If there is no value at that index or it isn't an integer this returns null
     *
     * @param index The index of the Setting
     * @return The value at the index
     */
    public Integer getInt(int index)
    {

        throw new UnsupportedOperationException();
    }

    /**
     * This returns the number of elements in this setting.
     *
     * @return The Number of elements in this setting.
     */
    public int getLength()
    {

        throw new UnsupportedOperationException();
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
            this.valuesBoolean.add(0, Utils.isTrue(initialValue, TRUE_VALUES));
        } else if (Convertible.toInt(initialValue))
        {
            this.valuesInteger.add(0, Integer.parseInt(initialValue));
        } else if (Convertible.toDouble(initialValue))
        {
            this.valuesDouble.add(0, Double.parseDouble(initialValue));
        } else if (Utils.isEnum(initialValue))
        {
            this.valuesEnum.add(0, Utils.getEnumElement(initialValue));
        }
    }

    /**
     * This return the Value of the Setting at the given index as Enum value. The first index is 0.
     * If there is no value at that index or it isn't Enum this returns null
     *
     * @param index The index of the Setting
     * @return The value at the index
     */
    public Enum getEnum(int index)
    {

        throw new UnsupportedOperationException();
    }

    /**
     * This sets that this setting was changed. Moreover it sets that any setting was changed. This can't be reversed.
     */
    protected void setSettingChanged()
    {

        changed = true;
        SettingsInformation.setAnySettingChanged();
    }

    /**
     * Adds a {@link ChangeListener} which will be notified whenever the value
     * of the {@code ObservableValue} changes. If the same listener is added
     * more than once, then it will be notified more than once. That is, no
     * check is made to ensure uniqueness.
     * <p>
     * Note that the same actual {@code ChangeListener} instance may be safely
     * registered for different {@code ObservableValues}.
     * <p>
     * The {@code ObservableValue} stores a strong reference to the listener
     * which will prevent the listener from being garbage collected and may
     * result in a memory leak. It is recommended to either unregister a
     * listener by calling {@link #removeListener(ChangeListener)
     * removeListener} after use or to use an instance of
     * {@link WeakChangeListener} avoid this situation.
     *
     * @param listener The listener to register
     *
     * @throws NullPointerException if the listener is null
     * @see #removeListener(ChangeListener)
     */
    @Override
    public void addListener(ChangeListener<? super String> listener)
    {
        valueString.addListener(listener);
    }

    /**
     * Removes the given listener from the list of listeners, that are notified
     * whenever the value of the {@code ObservableValue} changes.
     * <p>
     * If the given listener has not been previously registered (i.e. it was
     * never added) then this method call is a no-op. If it had been previously
     * added then it will be removed. If it had been added more than once, then
     * only the first occurrence will be removed.
     *
     * @param listener The listener to remove
     *
     * @throws NullPointerException if the listener is null
     * @see #addListener(ChangeListener)
     */
    @Override
    public void removeListener(ChangeListener<? super String> listener)
    {
        valueString.removeListener(listener);
    }

    /**
     * Adds an {@link InvalidationListener} which will be notified whenever the
     * {@code Observable} becomes invalid. If the same
     * listener is added more than once, then it will be notified more than
     * once. That is, no check is made to ensure uniqueness.
     * <p>
     * Note that the same actual {@code InvalidationListener} instance may be
     * safely registered for different {@code Observables}.
     * <p>
     * The {@code Observable} stores a strong reference to the listener
     * which will prevent the listener from being garbage collected and may
     * result in a memory leak. It is recommended to either unregister a
     * listener by calling {@link #removeListener(InvalidationListener)
     * removeListener} after use or to use an instance of
     * {@link WeakInvalidationListener} avoid this situation.
     *
     * @param listener The listener to register
     *
     * @throws NullPointerException if the listener is null
     * @see #removeListener(InvalidationListener)
     */
    @Override
    public void addListener(InvalidationListener listener)
    {
        valueString.addListener(listener);
    }

    /**
     * Removes the given listener from the list of listeners, that are notified
     * whenever the value of the {@code Observable} becomes invalid.
     * <p>
     * If the given listener has not been previously registered (i.e. it was
     * never added) then this method call is a no-op. If it had been previously
     * added then it will be removed. If it had been added more than once, then
     * only the first occurrence will be removed.
     *
     * @param listener The listener to remove
     *
     * @throws NullPointerException if the listener is null
     * @see #addListener(InvalidationListener)
     */
    @Override
    public void removeListener(InvalidationListener listener)
    {
        valueString.removeListener(listener);
    }

    /**
     * Get the wrapped value.
     *
     * @return The current value
     */
    @Override
    public String getValue()
    {
        return get();
    }

    /**
     * Set the wrapped value.
     *
     * @param value The new value
     */
    @Override
    public void setValue(String value)
    {
        set(value);
    }
}