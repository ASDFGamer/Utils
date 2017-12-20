package org.asdfgamer.utils.config;

import org.junit.Test;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;

public class PropertiesSpeicherTest implements IEinstellungen
{

    private final static Logger LOG = getLogger(PropertiesSpeicherTest.class.getName());

    public static final EinstellungenProperty einstellung = Einstellungen.neueEinstellung("es geht");

    public static final EinstellungenProperty einstellung2 = Einstellungen.neueEinstellung("es geht");

    @Test
    public void einstellungLaden()
    {

        assertEquals(false, einstellung.getEinstellungGeaendert());
        Einstellungen einstellungen = new Einstellungen("test");
        assertEquals(true, einstellungen.speichern(this));
        assertEquals(false, einstellung.getEinstellungGeaendert());
        einstellung.set("funktioniert nicht");
        assertEquals(true, einstellung.getEinstellungGeaendert());
        assertEquals(false, einstellung2.getEinstellungGeaendert());
        assertEquals(true, einstellungen.laden(this));
        assertEquals("es geht",einstellung.get());
        assertEquals(false, einstellung2.getEinstellungGeaendert());
    }

    @Test
    public void einstellungenLadenEnum()
    {
        Einstellungen einstellungen = new Einstellungen("testEnum");
        assertEquals(true, einstellungen.speichern(EinstellungEnum.class));
        EinstellungEnum.test.EINSTELLUNGProperty().set("funktioniert nicht");
        assertEquals(true, einstellungen.laden(EinstellungEnum.class));
        assertEquals("hallo", EinstellungEnum.test.EINSTELLUNGProperty().get());
    }
}