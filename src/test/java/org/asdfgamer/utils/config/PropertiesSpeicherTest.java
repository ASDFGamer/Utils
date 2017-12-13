package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertiesSpeicherTest implements IEinstellungen
{

    public static final EinstellungenProperty einstellung = Einstellungen.neueEinstellung("es geht");

    @Test
    public void einstellungLaden()
    {
        EinstellungenSpeicher speicher = new PropertiesSpeicher("test");
        assertEquals(false,speicher.getEinstellungen(this));//Da kein Progrname angegeben ist.
        assertEquals(true,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(false,PropertiesSpeicher.setProgrammName("test"));
        assertEquals(true,speicher.speichern(this));



    }
}