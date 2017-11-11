package org.asdfgamer.utils.config;

import static org.junit.Assert.*;
import org.junit.Test;

public class EinstellungenTest
{

    @Test
    public void neueEinstellung() throws Exception
    {
        EinstellungenProperty ein1 = Einstellungen.neueEinstellung(new Object[]{"test"});
        assertEquals("test",ein1.get());
        EinstellungenProperty ein2 = Einstellungen.neueEinstellung(new Object[]{5,0,9});
        assertEquals("5",ein2.get());
        assertEquals(true,ein2.hasIntegerValue());
        ein2.set("10");
        assertEquals(9l,(long)ein2.getInteger());
    }


}