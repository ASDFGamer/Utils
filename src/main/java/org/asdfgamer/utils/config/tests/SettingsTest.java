package org.asdfgamer.utils.config.tests;

import org.asdfgamer.utils.config.Setting;
import org.asdfgamer.utils.config.Settings;
import org.asdfgamer.utils.config.annotations.SettingInfo;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("WeakerAccess")
public class SettingsTest
{

    public final static Setting setting1 = Settings.newSetting(new Object[]{"test"});

    @SettingInfo(caption = "testSetting2", maximumValue = 9, minimumValue = 0)
    public final static Setting setting2 = Settings.newSetting(new Object[]{5, 0, 9});//TODO if a Setting is not static there are problems, for example with finding the annotations. There should be an better warning.

    @Test
    @Ignore
    public void newSetting()
    {

        assertEquals("test", setting1.get());

        assertFalse(setting2.getSettingChanged());
        assertEquals("5", setting2.get());
        assertTrue(setting2.hasIntegerValue());
        setting2.set("7");
        assertEquals(7L, (long) setting2.getInt());
        assertTrue(setting2.getSettingChanged());
        setting2.set("10");
        assertEquals(9L, (long) setting2.getInt());
        assertFalse(setting1.getSettingChanged());
        //TODO test if save(Object) works with a class Object of a normal class
    }


}