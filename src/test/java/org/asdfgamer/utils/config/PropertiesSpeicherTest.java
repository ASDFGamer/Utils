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
        EinstellungenSpeicher speicher = new PropertiesSpeicher("test");
        assertEquals(false,speicher.getEinstellungen(this));//Da kein Progrname angegeben ist.
        assertEquals(true,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(false,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(true,speicher.speichern(this));
        assertEquals(false, einstellung.getEinstellungGeaendert());
        einstellung.set("funktioniert nicht");
        assertEquals(true, einstellung.getEinstellungGeaendert());
        assertEquals(false, einstellung2.getEinstellungGeaendert());
        assertEquals(true, speicher.getEinstellungen(this));
        assertEquals("es geht",einstellung.get());
        assertEquals(false, einstellung2.getEinstellungGeaendert());
    }

    @Test
    public void einstellungenLadenEnum()
    {
        EinstellungenSpeicher speicher = new PropertiesSpeicher("testenum");
        PropertiesSpeicher.setProgrammName("test");
        assertEquals(true,speicher.speichern(EinstellungEnum.class));
        EinstellungEnum.test.EINSTELLUNGProperty().set("funktioniert nicht");
        assertEquals(true, speicher.getEinstellungen(EinstellungEnum.class));
        assertEquals("hallo", EinstellungEnum.test.EINSTELLUNGProperty().get());
    }
}