package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("WeakerAccess")
public class PropertiesSpeicherTest implements IEinstellungen
{
    public static final EinstellungenProperty einstellung = Einstellungen.neueEinstellung("es geht");

    public static final EinstellungenProperty einstellung2 = Einstellungen.neueEinstellung("es geht");

    @Test
    public void einstellungLaden()
    {
        einstellung.set("es geht");
        einstellung2.set("es geht");
        Einstellungen einstellungen = new Einstellungen("test");
        assertEquals(true, einstellungen.speichern(this));
        einstellung.set("funktioniert nicht");
        assertEquals(true, einstellungen.laden(this));
        assertEquals("es geht",einstellung.get());
        //TODO Einstellungen geändert wieder überprüfen
    }

    @Test
    public void einstellungenLadenEnum()
    {
        Einstellungen einstellungen = new Einstellungen("testEnum");
        EinstellungEnum.test.EINSTELLUNGProperty().set("hallo");
        assertEquals(true, einstellungen.speichern(EinstellungEnum.class));
        EinstellungEnum.test.EINSTELLUNGProperty().set("funktioniert nicht");
        assertEquals(true, einstellungen.laden(EinstellungEnum.class));
        assertEquals("hallo", EinstellungEnum.test.EINSTELLUNGProperty().get());
    }

    @Test
    public void alleEinstellungenLaden()
    {
        einstellung.set("Hurra");
        einstellung2.set("Hipp Hipp");
        EinstellungEnum.test.EINSTELLUNGProperty().set("Nun dies");
        Einstellungen einstellungen = new Einstellungen("test");
        assertEquals(true, einstellungen.speichern());
        einstellung.set("Schade");
        einstellung2.set("es geht nicht");
        EinstellungEnum.test.EINSTELLUNGProperty().set("obwohl es sollte");
        assertEquals(true, einstellungen.laden());
        assertEquals("Hurra", einstellung.get());
        assertEquals("Hipp Hipp", einstellung2.get());
        assertEquals("Nun dies", EinstellungEnum.test.EINSTELLUNGProperty().get());
    }
}