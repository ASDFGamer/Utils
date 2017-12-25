package org.asdfgamer.utils.config;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("WeakerAccess")
public class EinstellungenTest
{

    public EinstellungenProperty ein1 = Einstellungen.neueEinstellung(new Object[]{"test"});

    public EinstellungenProperty ein2 = Einstellungen.neueEinstellung(new Object[]{5, 0, 9});

    @Test
    public void neueEinstellung()
    {

        assertEquals("test",ein1.get());

        assertEquals(false, ein2.getEinstellungGeaendert());
        assertEquals("5",ein2.get());
        assertEquals(true,ein2.hasIntegerValue());
        ein2.set("7");
        assertEquals(7L, (long) ein2.getInteger());
        assertEquals(true, ein2.getEinstellungGeaendert());
        ein2.set("10");
        assertEquals(9L, (long) ein2.getInteger());
        assertEquals(false, ein1.getEinstellungGeaendert());
    }


}