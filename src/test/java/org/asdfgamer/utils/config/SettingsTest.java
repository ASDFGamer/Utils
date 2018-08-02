package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.asdfgamer.utils.config.TestEnum.*;
import static org.junit.Assert.assertFalse;

@SuppressWarnings("WeakerAccess")
public class SettingsTest
{

    public final Setting setting2 = Settings.newSetting(new Object[]{5, 0, 9});

    @Test
    public void StringSetting()
    {

        assertEquals("test", testString.getSETTING());
        assertEquals(false, testString.get().getSettingChanged());
        testString.get().set("Hello");
        assertEquals("Hello",testString.getSETTING());
        testString.get().setString("Moin",0);
        assertEquals("Moin",testString.getSETTING());
        assertEquals(true, testString.get().getSettingChanged());
        assertFalse(testString.get().set(1));
        assertFalse(testString.get().set(true));
        assertFalse(testString.get().set(testString));
        assertFalse(testString.get().set(3.14));
        assertEquals("testString",testString.get().getSettingName());
        //TODO test if save(Object) works with a class Object of a normal class
    }

    @Test
    public void IntSetting()
    {
        assertEquals("5", setting2.get());
        assertEquals(true, setting2.hasIntegerValue());
        setting2.set("7");
        assertEquals(7L, (long) setting2.getInt());
        assertEquals(true, setting2.getSettingChanged());
        setting2.set("10");
        assertEquals(10L, (long) setting2.getInt());
    }


}