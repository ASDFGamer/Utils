package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
        assertEquals(true, settings.save(this));
        setting1.set("doesn't work");
        assertEquals(true, settings.load(this));
        assertEquals("it works", setting1.get());
        //assertEquals(true,setting1.getSettingChanged());TODO Test for change - problem is that the order of tests is not specified
        //assertEquals(false,setting2.getSettingChanged());
    }

    @Test
    public void loadSettingsFromEnum()
    {
        Settings settings = new Settings("testEnum");
        TestEnum.test.SETTINGProperty().set("hello");
        assertEquals(true, settings.save(TestEnum.class));
        TestEnum.test.SETTINGProperty().set("doesn't work");
        assertEquals(true, settings.load(TestEnum.class));
        assertEquals("hello", TestEnum.test.SETTINGProperty().get());
    }

    @Test
    public void loadAllSettings()
    {
        setting1.set("Yeah");
        setting2.set("Hey Ho");
        TestEnum.test.SETTINGProperty().set("Now this");
        Settings settings = new Settings("test");
        assertEquals(true, settings.save());
        setting1.set("Sad");
        setting2.set("it doesn't work");
        TestEnum.test.SETTINGProperty().set("although it should");
        assertEquals(true, settings.load());
        assertEquals("Yeah", setting1.get());
        assertEquals("Hey Ho", setting2.get());
        assertEquals("Now this", TestEnum.test.SETTINGProperty().get());
    }
}