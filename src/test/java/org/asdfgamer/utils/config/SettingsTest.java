package org.asdfgamer.utils.config;

import org.junit.Test;

import java.lang.annotation.ElementType;

import static org.asdfgamer.utils.config.TestEnum.*;
import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
public class SettingsTest {

    @Test
    public void StringSetting() {

        assertEquals("test", testString.getSETTING());
        assertEquals(false, testString.get().getSettingChanged());
        testString.get().set("Hello");
        assertEquals("Hello", testString.getSETTING());
        testString.get().setString("Moin", 0);
        assertEquals("Moin", testString.getSETTING());
        assertEquals(true, testString.get().getSettingChanged());
        assertFalse(testString.get().set(1));
        assertFalse(testString.get().set(true));
        assertFalse(testString.get().set(testString));
        assertFalse(testString.get().set(3.14));
        assertEquals("testString", testString.get().getSettingName());
    }

    @Test
    public void IntSetting() {
        assertEquals("1", testInt.getSETTING());
        assertEquals(1,(long)testInt.get().getInt());
        assertEquals(false, testInt.get().getSettingChanged());
        testInt.get().set("3");
        assertEquals(3,(long)testInt.get().getInt());
        testInt.get().setInteger(5, 0);
        assertEquals(5,(long)testInt.get().getInt());
        assertEquals(true, testInt.get().getSettingChanged());
        boolean fehler = false;
        try {
            testInt.get().set("Hallo");
        } catch (IllegalArgumentException e) {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testInt.get().set(true));
        assertFalse(testInt.get().set(testString));
        assertFalse(testInt.get().set(3.14));
        assertEquals("testInt", testInt.get().getSettingName());
    }

    @Test
    public void BooleanSetting() {
        assertEquals("true", testBoolean.getSETTING());
        assertEquals(true,testBoolean.get().getBoolean());
        assertEquals(false, testBoolean.get().getSettingChanged());
        testBoolean.get().set("false");
        assertEquals(false,testBoolean.get().getBoolean());
        testBoolean.get().setBoolean(true, 0);
        assertEquals(true,testBoolean.get().getBoolean());
        assertEquals(true, testBoolean.get().getSettingChanged());
        boolean fehler = false;
        try {
            testBoolean.get().set("Hallo");
        } catch (IllegalArgumentException e) {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testBoolean.get().set(1));
        assertFalse(testBoolean.get().set(testString));
        assertFalse(testBoolean.get().set(3.14));
        assertEquals("testBoolean", testBoolean.get().getSettingName());
    }

    @Test
    public void EnumSetting() {
        assertEquals("java.lang.annotation.ElementType.ANNOTATION_TYPE", testEnum.getSETTING());
        assertEquals(ElementType.ANNOTATION_TYPE,testEnum.get().getEnum());
        assertEquals(false, testEnum.get().getSettingChanged());
        testEnum.get().set("java.lang.annotation.ElementType.FIELD");
        assertEquals(ElementType.FIELD,testEnum.get().getEnum());
        testEnum.get().setEnum(ElementType.LOCAL_VARIABLE, 0);
        assertEquals(ElementType.LOCAL_VARIABLE,testEnum.get().getEnum());
        assertEquals(true, testEnum.get().getSettingChanged());
        boolean fehler = false;
        try {
            testEnum.get().set("Hallo");
        } catch (IllegalArgumentException e) {
            fehler = true;
        }
        assertTrue(fehler);
        assertFalse(testEnum.get().set(true));
        assertFalse(testEnum.get().set(testEnum));
        assertFalse(testEnum.get().set(1));
        assertFalse(testEnum.get().set(3.14));
        assertEquals("testEnum", testEnum.get().getSettingName());
    }




}