package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("WeakerAccess")
public class PropertiesSpeicherTest
{
    public static final SettingsProperty einstellung = Settings.newSetting("es geht");

    public static final SettingsProperty einstellung2 = Settings.newSetting("es geht");

    @Test
    public void einstellungLaden()
    {
        einstellung.set("es geht");
        einstellung2.set("es geht");
        Settings einstellungen = new Settings("test");
        assertEquals(true, einstellungen.save(this));
        einstellung.set("funktioniert nicht");
        assertEquals(true, einstellungen.load(this));
        assertEquals("es geht",einstellung.get());
        //TODO Einstellungen geändert wieder überprüfen
    }

    @Test
    public void einstellungenLadenEnum()
    {
        Settings einstellungen = new Settings("testEnum");
        SettingsEnum.test.SETTINGProperty().set("hello");
        assertEquals(true, einstellungen.save(SettingsEnum.class));
        SettingsEnum.test.SETTINGProperty().set("funktioniert nicht");
        assertEquals(true, einstellungen.load(SettingsEnum.class));
        assertEquals("hello", SettingsEnum.test.SETTINGProperty().get());
    }

    @Test
    public void alleEinstellungenLaden()
    {
        einstellung.set("Hurra");
        einstellung2.set("Hipp Hipp");
        SettingsEnum.test.SETTINGProperty().set("Nun dies");
        Settings einstellungen = new Settings("test");
        assertEquals(true, einstellungen.save());
        einstellung.set("Schade");
        einstellung2.set("es geht nicht");
        SettingsEnum.test.SETTINGProperty().set("obwohl es sollte");
        assertEquals(true, einstellungen.load());
        assertEquals("Hurra", einstellung.get());
        assertEquals("Hipp Hipp", einstellung2.get());
        assertEquals("Nun dies", SettingsEnum.test.SETTINGProperty().get());
    }
}