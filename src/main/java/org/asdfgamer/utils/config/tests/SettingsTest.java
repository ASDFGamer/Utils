package org.asdfgamer.utils.config.tests;

import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.SettingsProperty;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("WeakerAccess")
public class SettingsTest
{

    public final SettingsProperty setting1 = Settings.newSetting(new Object[]{"test"});

    public final SettingsProperty setting2 = Settings.newSetting(new Object[]{5, 0, 9});

    @Test
    @Ignore
    public void newSetting()
    {

        assertEquals("test", setting1.get());

        assertEquals(false, setting2.getSettingChanged());
        assertEquals("5", setting2.get());
        assertEquals(true, setting2.hasIntegerValue());
        setting2.set("7");
        assertEquals(7L, (long) setting2.getInteger());
        assertEquals(true, setting2.getSettingChanged());
        setting2.set("10");
        assertEquals(9L, (long) setting2.getInteger());
        assertEquals(false, setting1.getSettingChanged());
        //TODO test if save(Object) works with a class Object of a normal class
    }


}