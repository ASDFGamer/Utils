package org.asdfgamer.utils.config;

import org.junit.Test;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;
import static org.junit.Assert.assertEquals;

public class PropertiesSpeicherTest implements IEinstellungen
{

    private final static Logger LOG = getLogger(PropertiesSpeicherTest.class.getName());

    public static final EinstellungenProperty einstellung = Einstellungen.neueEinstellung("es geht");

    @Test
    public void einstellungLaden()
    {
        EinstellungenSpeicher speicher = new PropertiesSpeicher("test");
        assertEquals(false,speicher.getEinstellungen(this));//Da kein Progrname angegeben ist.
        assertEquals(true,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(false,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(true,speicher.speichern(this));
        einstellung.set("funktioniert nicht");
        assertEquals(true, speicher.getEinstellungen(this));
        assertEquals("es geht",einstellung.get());
    }

    @Test
    public void einstellungenLadenEnum()
    {
        EinstellungenSpeicher speicher = new PropertiesSpeicher("testenum");
        PropertiesSpeicher.setProgrammName("test");
        assertEquals(true,speicher.speichern(EinstellungEnum.class));
    }
}