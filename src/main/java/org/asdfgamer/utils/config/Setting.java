package org.asdfgamer.utils.config;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.asdfgamer.utils.config.annotations.SettingInfo;
import org.asdfgamer.utils.config.internal.SettingUtils;
import org.asdfgamer.utils.config.internal.SettingsInformation;
import org.asdfgamer.utils.other.Convertible;
import org.asdfgamer.utils.other.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.asdfgamer.utils.config.internal.SettingUtils.*;

/**
 * This is the Property in which the setting gets saved.
 *
 * @author ASDFGamer
 * @version 0.9
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "WeakerAccess"})
public class Setting implements WritableStringValue, ObservableStringValue
{

    /**
     * The used Logger
     */
    private static final Logger LOG = Logger.getLogger(Setting.class.getName());

    /**
     * This is the default value for the Setting, interpreted as String.
     */
    private final String defaultValue;

    /**
     * This shows if this setting is only for internally use and shouldn't be saved, etc.
     */
    private Boolean internalValue;

    /**
     * This contains all information about this Setting that isn't connected to the value.
     */
    private SettingsInformation info;

    /**
     * This is the (first) value of the Setting
     */
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
     * This shows if the Minimum value was used.
     */
    private boolean minimumNeverUsed = true;

    /**
     * This shows if the Maximum value was used.
     */
    private boolean maximumNeverUsed = true;

    // ----Constructors----

    /**
     * This is the default constructor, everything uses default values.
     */
    Setting(SettingsInformation info)
    {
        this.info = info;
        info.setSettings(this);
        this.defaultValue = null;
        this.valueString = new SimpleStringProperty();
        this.valuesString.addListener(getValueUpdater());
    }

    /**
     * This Constructor uses a default/initial value, everything else uses default values.
     * If the value can be interpreted as an integer, double or boolean value, the setting will do so and accept only
     * values of that type afterwards.
     *
     * @param initialValue The default/initial value of the setting.
     */
    Setting(String initialValue)
    {
        this.valuesString.add(initialValue);
        this.defaultValue = initialValue;
        this.valueString = new SimpleStringProperty(initialValue);
        this.valuesString.addListener(getValueUpdater());
        this.init(initialValue);
    }

    /**
     * This constructor receives the default/initial value and if this setting is internal, everything else uses
     * default values.
     *
     * @param initialValue  The default/initial value of the setting.
     * @param internalValue This shows if the Setting is only for internal use.
     */
    Setting(String initialValue, boolean internalValue, SettingsInformation info)
    {
        this.info = info;
        info.setSettings(this);
        this.valuesString.add(initialValue);
        this.valueString = new SimpleStringProperty(initialValue);
        this.defaultValue = initialValue;
        this.valuesString.addListener(getValueUpdater());
        this.init(initialValue);
        this.internalValue = internalValue;
        info.setSettings(this);
    }

    /**
     * This Constructor uses a default/initial value, everything else uses default values.
     * If the value can be interpreted as an integer, double or boolean value, the setting will do so and accept only
     * values of that type afterwards.
     *
     * @param initialValue The default/initial value of the setting.
     */
    Setting(List<String> initialValue, SettingsInformation info)
    {
        this.info = info;
        info.setSettings(this);
        this.init(initialValue);
        //this.valuesString = FXCollections.observableList(initialValue);
        this.defaultValue = initialValue.get(0);
        this.valueString = new SimpleStringProperty(initialValue.get(0));
        this.valuesString.addListener(getValueUpdater());

        this.internalValue = false;
    }

    /**
     * This constructor receives the default/initial value and if this setting is internal, everything else uses
     * default values.
     *
     * @param initialValue  The default/initial value of the setting.
     * @param internalValue This shows if the Setting is only for internal use.
     */
    Setting(List<String> initialValue, boolean internalValue, SettingsInformation info)
    {
        this.info = info;
        info.setSettings(this);
        this.init(initialValue);
        //this.valuesString = FXCollections.observableList(initialValue);This should be already done in init()
        this.valueString = new SimpleStringProperty(initialValue.get(0));
        this.defaultValue = initialValue.get(0);
        this.valuesString.addListener(getValueUpdater());
        this.internalValue = internalValue;
    }

    // ----Getter----
    // ---Get Values---
    // --Get String--

    /**
     * Get the wrapped value.
     *
     * @return The current value
     */
    @Override
    public String getValue()
    {
        return getString();
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
        return getString();
    }

    /**
     * TODO
     *
     * @param index
     * @return
     */
    public String get(int index)
    {
        return getString(index);
    }

    /**
     * This Methode returns the Setting as String. If there are more entries, then the first entry is returned.
     *
     * @return The value of the Setting as String
     */
    public String getString()
    {

        return getString(0);
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
        if (valuesString.size() > index)
        {
            return valuesString.get(index);
        } else
        {
            LOG.warning(bundle.getString("indexToHigh"));
            return null;
        }

    }

    // --Get Int--

    /**
     * This returns the integer value of the setting.
     *
     * @return The integer value of the Setting or 'null' if the setting has no integer value.
     */
    public Integer getInt()
    {

        return getInt(0);
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

        if (valuesInteger.size() > index)
        {
            return valuesInteger.get(index);
        } else
        {
            LOG.warning(bundle.getString("indexToHigh"));
            return null;
        }
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

    // --Get Double--

    /**
     * This returns the double value of the setting. If there is no double value it returns the integer value.
     * If there is also no integer value then it returns 'null'.
     *
     * @return This returns the double value of the setting. If there is no double value it returns the integer value.
     * If there is also no integer value then it returns 'null'.
     */
    public Double getDouble()
    {

        return getDouble(0);
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

        if (valuesDouble.size() > index)
        {
            return valuesDouble.get(index);
        } else
        {
            LOG.warning(bundle.getString("indexToHigh"));
            return getInt(index).doubleValue();
        }
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

    // --Get Boolean--

    /**
     * This returns the boolean value of the setting.
     *
     * @return The boolean value of the Setting or 'null' if the setting has no boolean value.
     */
    public Boolean getBoolean()
    {

        return getBoolean(0);
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

        if (valuesBoolean.size() > index)
        {
            return valuesBoolean.get(0);
        } else
        {
            LOG.warning(bundle.getString("indexToHigh"));
            return null;
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

    // --Get Enum--

    /**
     * This return the Value of the Setting as Enum value. If this Setting has no Enum value this returns null.
     *
     * @return The value at the index
     */
    public Enum getEnum()
    {
        return getEnum(0);
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

        if (valuesEnum.size() > index)
        {
            return valuesEnum.get(0);
        } else
        {
            LOG.warning(bundle.getString("indexToHigh"));
            return null;
        }
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

    // ---Get Other---

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
     * This returns the Classname of this Property.
     *
     * @return the Classname of the class that created this property.
     */
    public String getClassName()
    {

        return info.getClassName();
    }

    /**
     * This returns the maximum for this setting, if one exists.
     *
     * @return The maximum of this setting or null if it doesn't exists.
     */
    public Double getMaximum()
    {
        if (maximum == null & maximumNeverUsed)
        {
            try
            {
                SettingInfo annotation = SettingUtils.getAnnotation(this);//TODO when this is called to early it returns null, but sometimes it should return null
                if (annotation != null && annotation.maximumValue() != Double.MAX_VALUE)
                {
                    maximum = annotation.maximumValue();
                }
                maximumNeverUsed = false;
            } catch (IllegalStateException e)
            {
                LOG.fine(e.getMessage());
                return null;
            }

        }
        return maximum;
    }

    /**
     * This returns the minimum for this setting, if one exists.
     *
     * @return The minimum of this setting or null if it doesn't exists.
     */
    public Double getMinimum()
    {
        if (minimum == null & minimumNeverUsed)
        {
            try
            {

                SettingInfo annotation = SettingUtils.getAnnotation(this);
                if (annotation != null && annotation.minimumValue() != Double.MIN_VALUE)
                {
                    minimum = annotation.minimumValue();
                }
                minimumNeverUsed = false;
            } catch (IllegalStateException e)
            {
                LOG.fine(e.getMessage());
                return null;
            }
        }
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
     * @throws IllegalStateException if there was a problem with the setting, for example if it isn't fully instantiated.
     */
    public String getSettingName()
    {
        return info.getSettingName();
    }

    /**
     * This shows if a Setting is only for internal use. This means for example that it shouldn't be saved.
     *
     * @return true, if it is only an internal value, otherwise false.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInternalValue()
    {
        if (internalValue == null)
        {
            SettingInfo annotation = SettingUtils.getAnnotation(this);
            if (annotation != null)
            {
                internalValue = annotation.internalValue();
            }
        }
        return internalValue;
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
     * This returns the default value.
     *
     * @return The default value.
     */
    public String getDefaultValue()//TODO how works this in Lists?
    {

        return defaultValue;
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
     * This returns the number of elements in this setting.
     *
     * @return The Number of elements in this setting.
     */
    public int getLength()
    {

        return valuesString.size();
    }

    // ----Setter----
    // ---Set Values---
    // --Set String--

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

    /**
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * The methode {@link Setting#setString(String)} does exactly the same thing, but returns an boolean value
     * instead of the exception.
     *
     * @param newValue The new value that should be assigned.
     * @throws IllegalArgumentException This happens if the setting has for example an integer value and
     *                                  the new value is a 'pure' string or boolean or double.
     */
    @Override
    public void set(String newValue) throws IllegalArgumentException
    {
        set(newValue, 0);
    }

    /**
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * The methode {@link Setting#setString(String)} does exactly the same thing, but returns an boolean value
     * instead of the exception.
     *
     * @param newValue The new value that should be assigned.
     * @param index    The index where the value should be added or updated.
     * @throws IllegalArgumentException  This happens if the setting has for example an integer value and
     *                                   the new value is a 'pure' string or boolean or double.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public void set(String newValue, int index) throws IllegalArgumentException, IndexOutOfBoundsException
    {

        if (this.valuesBoolean.size() > 0)
        {
            if (Convertible.toBoolean(newValue, TRUE_VALUES, FALSE_VALUES))
            {
                setBoolean(Utils.isTrue(newValue, TRUE_VALUES), index);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_boolean"));
            }
        } else if (this.valuesInteger.size() > 0)
        {
            if (Convertible.toInt(newValue))
            {
                setInteger(Integer.parseInt(newValue), index);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_integer"));
            }
        } else if (this.valuesDouble.size() > 0)
        {
            if (Convertible.toDouble(newValue))
            {
                setDouble(Double.valueOf(newValue), index);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + bundle.getString("cantConvertValue_double"));
            }
        } else if (this.valuesEnum.size() > 0)
        {
            if (!newValue.contains("."))
            {
                newValue = defaultValue.substring(0, defaultValue.lastIndexOf("."))+"."+newValue;
            }
            if (Utils.isEnum(newValue))
            {
                setEnum(Utils.getEnumElement(newValue), index);
            } else
            {
                throw new IllegalArgumentException(bundle.getString("cantConvertValue_start") + newValue + " " + bundle.getString("cantConvertValue_enum"));
            }
        } else
        {//this gets set in every other set-operation ...
            setOnlyString(newValue, index);
        }

    }

    /**
     * This sets the value only for the String.
     *
     * @param newValue The new value
     * @param index    The index
     */
    private void setOnlyString(String newValue, int index)
    {
        if (index == valuesString.size())
        {
            valuesString.add(newValue);
        } else
        {
            valuesString.set(index, newValue);//TODO only if it changed
        }
    }

    /**
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * This is exactly the same as the {@link Setting#set(String)} methode but if that methode throws an
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
     * This sets the new value of the setting and updates, if necessary, the boolean, integer or double value.
     * This is exactly the same as the {@link Setting#set(String)} methode but if that methode throws an
     * exception this methode returns false.
     *
     * @param newValue The new value that should be assigned.
     * @param index    The index of the Setting.
     * @return false, if the setting has for example an integer value an the new value is a 'pure' string or boolean
     * or double.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public boolean setString(String newValue, int index) throws IndexOutOfBoundsException
    {
        try
        {
            set(newValue, index);
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    private boolean add(String newValue)
    {
        return addString(newValue);
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    public boolean addString(String newValue)
    {
        try
        {
            set(newValue, getLength());
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    // --Set Int--

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
     * TODO
     *
     * @param newValue
     * @param index
     * @return
     */
    public boolean set(Integer newValue, int index)
    {

        return setInteger(newValue, index);
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
        return setInteger(newValue, 0);
    }

    /**
     * This sets the integer value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as a integer.
     *
     * @param newValue The new value.
     * @param index    The index of the Setting
     * @return true, if the value changed, otherwise false.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public boolean setInteger(int newValue, int index) throws IndexOutOfBoundsException
    {

        if (this.valuesInteger.size() > 0)
        {
            if (getMaximum() != null && getMaximum() < newValue)
            {
                newValue = getMaximum().intValue();
            } else if (getMinimum() != null && getMinimum() > newValue)
            {
                newValue = getMinimum().intValue();
            }
            if (index == valuesInteger.size())
            {
                this.valuesInteger.add(newValue);
            } else
            {
                this.valuesInteger.set(index, newValue);
            }
            setOnlyString(String.valueOf(newValue), index);
            return true;
        } else
        {
            LOG.warning(bundle.getString("noIntegerValue"));
            return false;
        }
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    public boolean addInteger(int newValue)
    {
        try
        {
            setInteger(newValue, getLength());
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    // --Set Double--

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
     * TODO
     *
     * @param newValue
     * @param index
     * @return
     */
    public boolean set(Double newValue, int index)
    {

        return setDouble(newValue,index);
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

        return setDouble(newValue, 0);
    }

    /**
     * This sets the double value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as double
     * (and not as integer).
     *
     * @param newValue The new value.
     * @param index    The index of the Setting
     * @return true, if the value changed, otherwise false.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public boolean setDouble(double newValue, int index) throws IndexOutOfBoundsException
    {

        if (this.valuesDouble.size() > 0)
        {
            if (getMaximum() != null && getMaximum() < newValue)
            {
                newValue = getMaximum();
            } else if (getMinimum() != null && getMinimum() > newValue)
            {
                newValue = getMinimum();
            }
            if (index == valuesDouble.size())
            {
                this.valuesDouble.add(newValue);
            } else
            {
                this.valuesDouble.set(index, newValue);
            }
            setOnlyString(String.valueOf(newValue), index);

            return true;
        } else
        {
            LOG.warning(bundle.getString("noDoubleValue"));
            return false;
        }
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    public boolean addDouble(double newValue)
    {
        try
        {
            setDouble(newValue, getLength());
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    // --Set Boolean--

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
     * TODO
     *
     * @param newValue
     * @param index
     * @return
     */
    public boolean set(Boolean newValue, int index)
    {

        return setBoolean(newValue,index);
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

        return setBoolean(newValue, 0);

    }

    /**
     * This sets the boolean value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as boolean.
     *
     * @param newValue The new value.
     * @param index    The index of the Setting
     * @return true, if the value changed, otherwise false.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public boolean setBoolean(boolean newValue, int index) throws IndexOutOfBoundsException
    {

        if (this.valuesBoolean.size() > 0)
        {
            if (index == valuesBoolean.size())
            {
                this.valuesBoolean.add(newValue);
            } else
            {
                this.valuesBoolean.set(index, newValue);
            }
            setOnlyString(String.valueOf(newValue), index);

            return true;
        } else
        {
            LOG.warning(bundle.getString("noBooleanValue"));
            return false;
        }
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    public boolean addBoolean(boolean newValue)
    {
        try
        {
            setBoolean(newValue, getLength());
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    // --Set Enum--

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
     * TODO
     *
     * @param newValue
     * @param index
     * @return
     */
    public boolean set(Enum newValue, int index)
    {

        return setEnum(newValue,index);
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

        return setEnum(newValue, 0);
    }

    /**
     * This sets the Enum value of the setting and updates the String-value.
     * <p>
     * Note: This is only successful, if the setting got initialized with an value that can be interpreted as an Enum.
     *
     * @param newValue The new value.
     * @param index    The index of the Setting
     * @return true, if the value changed, otherwise false.
     * @throws IndexOutOfBoundsException This happens if the given index isn't already set or the next free value.
     */
    public boolean setEnum(Enum newValue, int index) throws IndexOutOfBoundsException
    {

        if (this.valuesEnum.size() > 0)
        {
            if (index == valuesEnum.size())
            {
                this.valuesEnum.add(newValue);
            } else
            {
                this.valuesEnum.set(index, newValue);
            }
            setOnlyString(String.valueOf(newValue), index);

            return true;
        } else
        {
            LOG.warning(bundle.getString("noEnumValue"));
            return false;
        }
    }

    /**
     * This adds an new Setting at the end of the List
     *
     * @param newValue the added Setting.
     * @return true, if it was successful, otherwise false.
     */
    public boolean addEnum(Enum newValue)
    {
        try
        {
            setEnum(newValue, getLength());
        } catch (IllegalArgumentException e)
        {
            return false;
        }
        return true;
    }

    // ---Set Other---

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
     * This resets the setting to the default value.
     */
    public void setToDefaultValue()
    {

        set(getDefaultValue());
    }

    /**
     * This sets that this setting was changed. Moreover it sets that any setting was changed. This can't be reversed.
     */
    protected void setSettingChanged()
    {

        changed = true;
        SettingsInformation.setAnySettingChanged();
    }

    // ----Other Methods----
    // ---Listeners---

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
     * @throws NullPointerException if the listener is null
     * @see #addListener(ChangeListener)
     */
    @Override
    public void removeListener(ChangeListener<? super String> listener)
    {
        valueString.removeListener(listener);
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
     * @throws NullPointerException if the listener is null
     * @see #removeListener(ChangeListener)
     */
    public void addListener(ListChangeListener<? super String> listener)
    {
        valuesString.addListener(listener);
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
     * @throws NullPointerException if the listener is null
     * @see #addListener(ChangeListener)
     */
    public void removeListener(ListChangeListener<? super String> listener)
    {
        valuesString.removeListener(listener);
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
     * @throws NullPointerException if the listener is null
     * @see #addListener(InvalidationListener)
     */
    @Override
    public void removeListener(InvalidationListener listener)
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
     * @param toList   This shows if the Listener should be added to the List with all Values or to the first String
     * @throws NullPointerException if the listener is null
     * @see #removeListener(InvalidationListener)
     */
    public void addListener(InvalidationListener listener, boolean toList)
    {
        if (toList)
        {
            valuesString.addListener(listener);
        } else
        {
            addListener(listener);
        }
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
     * @param toList   This shows if the Listener should be added to the List with all Values or to the first String
     * @throws NullPointerException if the listener is null
     * @see #addListener(InvalidationListener)
     */
    public void removeListener(InvalidationListener listener, boolean toList)
    {
        if (toList)
        {
            valuesString.removeListener(listener);
        } else
        {
            removeListener(listener);
        }

    }

    private ListChangeListener<String> getValueUpdater()
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
     * This Methode gets called from the Constructor to check if the initial value convertible to boolean, integer or
     * double. If so it saves the value in that format.
     *
     * @param initialValue The initial Value of the setting.
     */
    private void init(String initialValue)
    {
        this.valuesString.add(0, initialValue);
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
     * This Methode gets called from the Constructor to check if the initial value convertible to boolean, integer or
     * double. If so it saves the value in that format.
     * If the first value is convertible to one value and another following value to an other value, this will throw an exception
     *
     * @param initialValue The initial Value of the setting.
     * @throws IllegalArgumentException If there is an different value Type then the first type in the list.
     */
    private void init(List<String> initialValue)
    {
        init(initialValue.get(0));
        for (int i = 1; i < initialValue.size(); i++)
        {
            set(initialValue.get(i), i);
        }
    }
}