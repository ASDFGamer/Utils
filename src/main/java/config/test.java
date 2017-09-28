package config;

import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

public class test implements IEinstellungen
{

    private final static Logger LOG = getLogger(test.class.getName());

    public static EinstellungenProperty test = Einstellungen.neueEinstellung("test");

    public static EinstellungenProperty testint = Einstellungen.neueEinstellung(1);

    public static EinstellungenProperty testfloat = Einstellungen.neueEinstellung(1.1);

    private static Einstellungen einstellungen = new Einstellungen("test");

    public static void main(String[] args)
    {

        LOG.info("test.main Zeile 12");
        LOG.info(test.get());
        einstellungen.lade(new test());
        LOG.info(test.get());
        einstellungen.speicher(new test());
        LOG.info("test.main Zeile 15");
    }


}
