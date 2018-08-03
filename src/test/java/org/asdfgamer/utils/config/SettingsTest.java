package org.asdfgamer.utils.config;

import org.junit.Test;

import java.lang.annotation.ElementType;

import static org.asdfgamer.utils.config.TestEnum.*;
import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
public class SettingsTest
{

    private static final double DELTA = 0.000001;

    @Test
    public void StringSetting()
    {

        assertEquals("test", testString.getSETTING());
        assertFalse(testString.get().getSettingChanged());
        testString.get().set("Hello");
        assertEquals("Hello", testString.getSETTING());
        testString.get().setString("Moin", 0);
        assertEquals("Moin", testString.getSETTING());
        assertTrue(testString.get().getSettingChanged());
        assertFalse(testString.get().set(1));
        assertFalse(testString.get().set(true));
        assertFalse(testString.get().set(testString));
        assertFalse(testString.get().set(3.14));
        assertEquals("testString", testString.get().getSettingName());
    }

    @Test
    public void IntSetting()
    {
        assertEquals("1", testInt.getSETTING());
        assertEquals(1, (long) testInt.get().getInt());
        assertFalse(testInt.get().getSettingChanged());
        testInt.get().set("3");
        assertEquals(3, (long) testInt.get().getInt(0));
        testInt.get().setInteger(5, 0);
        assertEquals(5, (long) testInt.get().getInt());
        assertTrue(testInt.get().getSettingChanged());
        boolean fehler = false;
        try
        {
            testInt.get().set("Hallo");
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testInt.get().set(true));
        assertFalse(testInt.get().set(testString));
        assertFalse(testInt.get().set(3.14));
        assertEquals("testInt", testInt.get().getSettingName());
    }

    @Test
    public void DoubleSetting()
    {
        assertEquals("1.1", testDouble.getSETTING());
        assertEquals(1.1, testDouble.get().getDouble(), DELTA);
        assertFalse(testDouble.get().getSettingChanged());
        testDouble.get().set("3.14");
        assertEquals(3.14, testDouble.get().getDouble(), DELTA);
        testDouble.get().setInteger(5, 0);
        assertEquals(5, testDouble.get().getDouble(), DELTA);
        testDouble.get().setDouble(5.01, 0);
        assertEquals(5.01, testDouble.get().getDouble(0), DELTA);
        assertTrue(testDouble.get().getSettingChanged());
        boolean fehler = false;
        try
        {
            testDouble.get().set("Hallo");
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testDouble.get().set(true));
        assertFalse(testDouble.get().set(testString));
        assertEquals("testDouble", testDouble.get().getSettingName());
    }

    @Test
    public void BooleanSetting()
    {
        assertEquals("true", testBoolean.getSETTING());
        assertEquals(true, testBoolean.get().getBoolean());
        assertFalse(testBoolean.get().getSettingChanged());
        testBoolean.get().set("false");
        assertEquals(false, testBoolean.get().getBoolean(0));
        testBoolean.get().setBoolean(true, 0);
        assertEquals(true, testBoolean.get().getBoolean());
        assertTrue(testBoolean.get().getSettingChanged());
        boolean fehler = false;
        try
        {
            testBoolean.get().set("Hallo");
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testBoolean.get().set(1));
        assertFalse(testBoolean.get().set(testString));
        assertFalse(testBoolean.get().set(3.14));
        assertEquals("testBoolean", testBoolean.get().getSettingName());
    }

    @Test
    public void EnumSetting()
    {
        assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE", testEnum.getSETTING());
        assertEquals(ElementType.ANNOTATION_TYPE, testEnum.get().getEnum(0));
        assertFalse(testEnum.get().getSettingChanged());
        testEnum.get().set("java.lang.annotation.ElementType.FIELD");
        assertEquals(ElementType.FIELD, testEnum.get().getEnum());
        testEnum.get().setEnum(ElementType.LOCAL_VARIABLE, 0);
        assertEquals(ElementType.LOCAL_VARIABLE, testEnum.get().getEnum());
        assertTrue(testEnum.get().getSettingChanged());
        boolean fehler = false;
        try
        {
            testEnum.get().set("Hallo");
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testEnum.get().set(true));
        assertFalse(testEnum.get().set(testEnum));
        assertFalse(testEnum.get().set(1));
        assertFalse(testEnum.get().set(3.14));
        assertEquals("testEnum", testEnum.get().getSettingName());
    }


    @Test
    public void StringListSetting()
    {
        assertEquals("Dies", testListString.get().get(0));
        assertEquals("ist", testListString.get().get(1));
        assertEquals("ein", testListString.get().get(2));
        assertEquals("test", testListString.get().get(3));
        assertFalse(testListString.get().getSettingChanged());
        assertEquals(4, testListString.get().getLength());
        testListString.get().set("Ja");
        assertTrue(testListString.get().getSettingChanged());
        assertEquals("Ja", testListString.get().get());
        assertEquals(4, testListString.get().getLength());
        testListString.get().set(".", 4);
        assertEquals(".", testListString.get().get(4));
        assertEquals(5, testListString.get().getLength());

        assertFalse(testListString.get().set(true, 5));
        assertFalse(testListString.get().set(testEnum, 5));
        assertFalse(testListString.get().set(1, 5));
        assertFalse(testListString.get().set(3.14, 5));
    }

    @Test
    public void IntListSetting()
    {
        assertEquals("1", testListInt.get().get());
        assertEquals("2", testListInt.get().get(1));
        assertEquals(3, (int) testListInt.get().getInt(2));
        assertFalse(testListInt.get().getSettingChanged());
        assertEquals(3, testListInt.get().getLength());
        testListInt.get().set(0);
        assertTrue(testListInt.get().getSettingChanged());
        assertEquals("0", testListInt.get().get());
        assertEquals(3, testListInt.get().getLength());
        testListInt.get().set("4", 3);
        assertEquals(4, (int) testListInt.get().getInt(3));
        assertEquals(4, testListInt.get().getLength());

        boolean fehler = false;
        try
        {
            testListInt.get().set("Hallo", 4);
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testListInt.get().set(true, 4));
        assertFalse(testListInt.get().set(testEnum, 4));
        assertFalse(testListInt.get().set(3.14, 4));
    }

    @Test
    public void DoubleListSetting()
    {
        assertEquals("1.1", testListDouble.get().get());
        assertEquals("2.2", testListDouble.get().get(1));
        assertEquals(3.3, testListDouble.get().getDouble(2), DELTA);
        assertFalse(testListDouble.get().getSettingChanged());
        assertEquals(3, testListDouble.get().getLength());
        testListDouble.get().set(-4.4);
        assertTrue(testListDouble.get().getSettingChanged());
        assertEquals("-4.4", testListDouble.get().get());
        assertEquals(3, testListDouble.get().getLength());
        testListDouble.get().set(5.5, 3);
        assertEquals(5.5, testListDouble.get().getDouble(3), DELTA);
        assertEquals(4, testListDouble.get().getLength());

        boolean fehler = false;
        try
        {
            testListDouble.get().set("Hallo", 4);
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testListDouble.get().set(true, 4));
        assertFalse(testListDouble.get().set(testEnum, 4));
        assertTrue(testListDouble.get().set(3, 4));
    }

    @Test
    public void BooleanListSetting()
    {
        assertEquals("true", testListBoolean.get().get());
        assertEquals(false, testListBoolean.get().getBoolean(1));
        assertFalse(testListBoolean.get().getSettingChanged());
        assertEquals(2, testListBoolean.get().getLength());
        testListBoolean.get().set("false");
        assertTrue(testListBoolean.get().getSettingChanged());
        assertEquals(false, testListBoolean.get().getBoolean());
        assertEquals(2, testListBoolean.get().getLength());
        testListBoolean.get().set(false, 2);
        assertEquals("false", testListBoolean.get().get(2));
        assertEquals(3, testListBoolean.get().getLength());

        boolean fehler = false;
        try
        {
            testListBoolean.get().set("Hallo", 3);
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testListBoolean.get().set(2.12, 3));
        assertFalse(testListBoolean.get().set(testEnum, 3));
        assertFalse(testListBoolean.get().set(3, 3));
    }

    @Test
    public void EnumListSetting()
    {
        assertEquals("org.asdfgamer.utils.config.TestEnum.testString", testListEnum.get().get());
        assertEquals("org.asdfgamer.utils.config.TestEnum.testInt", testListEnum.get().get(1));
        assertEquals(testDouble, testListEnum.get().getEnum(2));
        assertFalse(testListEnum.get().getSettingChanged());
        assertEquals(3, testListEnum.get().getLength());
        testListEnum.get().set(testBoolean);
        assertTrue(testListEnum.get().getSettingChanged());
        assertEquals("org.asdfgamer.utils.config.TestEnum.testBoolean", testListEnum.get().get());
        assertEquals(3, testListEnum.get().getLength());
        testListEnum.get().set("org.asdfgamer.utils.config.TestEnum.testEnum", 3);
        assertEquals(testEnum, testListEnum.get().getEnum(3));
        assertEquals(4, testListEnum.get().getLength());

        boolean fehler = false;
        try
        {
            testListEnum.get().set("Hallo", 4);
        } catch (IllegalArgumentException e)
        {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testListEnum.get().set(true, 4));
        assertFalse(testListEnum.get().set(ElementType.LOCAL_VARIABLE, 4));
        assertFalse(testListEnum.get().set(3.14, 4));
        assertFalse(testListEnum.get().set(3, 4));
    }

    @Test
    public void IntSettingWithAnnotation()
    {
        assertEquals(2,(int)testIntAnnotation.get().getInt());
        assertEquals(-5, testIntAnnotation.get().getMinimum(),DELTA);
        assertEquals(6, testIntAnnotation.get().getMaximum(),DELTA);
        assertTrue(testIntAnnotation.get().isInternalValue());
        assertEquals("This is a simple Info.",testIntAnnotation.get().getInformationText());
        testIntAnnotation.get().set(6);
        assertEquals("6",testIntAnnotation.getSETTING());
        testIntAnnotation.get().set(42);
        assertEquals("6",testIntAnnotation.getSETTING());
        testIntAnnotation.get().set(-5);
        assertEquals("-5",testIntAnnotation.getSETTING());
        testIntAnnotation.get().set(-42);
        assertEquals("-5",testIntAnnotation.getSETTING());
        assertFalse(testIntAnnotation.get().setMaximumValue(-6));
        assertEquals(6, testIntAnnotation.get().getMaximum(),DELTA);
        assertFalse(testIntAnnotation.get().setMinimumValue(6.1));
        assertEquals(-5, testIntAnnotation.get().getMinimum(),DELTA);
    }


}