package org.asdfgamer.utils.config;

/**
 * Dies ist eine Beispielimplementation des Interfaces {@link IEinstellungen}.
 */
public enum EinstellungEnum implements IEinstellungen
{
    test("hallo");

    private final EinstellungenProperty EINSTELLUNG;

    /**
     * Dieser Konstruktor ist für Enums wichtig, damit nur ein einziger Konstruktor für alle möglichen Fälle verwendet
     * werden kann.
     * Der Nachteil dieses Konstruktors ist, dass er Fehler erst nach der Ausführung anzeigen kann und nicht schon
     * z.B. an den Paramentern erkennen kann.
     *
     * @param args Dies sind die Argumente mit den eine neue Einstellung erzeugt werden soll.
     */
    EinstellungEnum(Object... args)
    {

        EINSTELLUNG = Einstellungen.neueEinstellung(args);
    }

    /*
     * Theoretisch wäre von den folgenden Gettern/Settern nur der Getter für die Property notwendig, da es für die Getter
     * und Setter noch andere Methoden gibt, die andere Typen annehmen/ausgeben.
     */
    public String getEINSTELLUNG()
    {

        return EINSTELLUNG.get();
    }

    public EinstellungenProperty EINSTELLUNGProperty()
    {

        return EINSTELLUNG;
    }

    public void setEINSTELLUNG(String EINSTELLUNG)
    {

        this.EINSTELLUNG.set(EINSTELLUNG);
    }
}
