package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EinstellungenTest
{

    @Test
    public void neueEinstellung() throws Exception
    {
        EinstellungenProperty ein1 = Einstellungen.neueEinstellung(new Object[]{"test"});
        assertEquals("test",ein1.get());
        EinstellungenProperty ein2 = Einstellungen.neueEinstellung(new Object[]{5,0,9});
        assertEquals(false, ein2.getEinstellungGeaendert());
        assertEquals("5",ein2.get());
        assertEquals(true,ein2.hasIntegerValue());
        ein2.set("7");
        assertEquals(7l, (long) ein2.getInteger());
        assertEquals(true, ein2.getEinstellungGeaendert());
        ein2.set("10");
        assertEquals(9l,(long)ein2.getInteger());
        assertEquals(false, ein1.getEinstellungGeaendert());
    }


}