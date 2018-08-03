package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SuppressWarnings("WeakerAccess")
public class PropertiesFileStorageTest
{
    public static final Setting setting1 = Settings.newSetting("it works");

    public static final Setting setting2 = Settings.newSetting("it works really");

    @Test
    public void loadSettings()
    {
        setting1.set("it works");
        setting2.set("it works really");
        Settings settings = new Settings("test");
        assertTrue(settings.save(this));
        setting1.set("doesn't work");
        assertTrue(settings.load(this));
        assertEquals("it works", setting1.get());
        //assertEquals(true,setting1.getSettingChanged());TODO Test for change - problem is that the order of tests is not specified
        //assertEquals(false,setting2.getSettingChanged());
    }

    @Test
    public void loadSettingsFromEnum()
    {
        Settings settings = new Settings("testEnum");
        TestEnum.testString.SETTINGProperty().set("hello");
        assertTrue(settings.save(TestEnum.class));
        TestEnum.testString.SETTINGProperty().set("doesn't work");
        assertTrue(settings.load(TestEnum.class));
        assertEquals("hello", TestEnum.testString.SETTINGProperty().get());
        //TODO test if save(Object) works with a class Object of a normal class
    }

    @Test
    public void loadAllSettings()
    {
        setting1.set("Yeah");
        setting2.set("Hey Ho");
        TestEnum.testString.SETTINGProperty().set("Now this");
        Settings settings = new Settings("testString");
        assertTrue(settings.save());
        setting1.set("Sad");
        setting2.set("it doesn't work");
        TestEnum.testString.SETTINGProperty().set("although it should");
        assertTrue(settings.load());
        assertEquals("Yeah", setting1.get());
        assertEquals("Hey Ho", setting2.get());
        assertEquals("Now this", TestEnum.testString.SETTINGProperty().get());
    }
}