package other;

/**
 * Hirmit kann überprüft werden ob ein String in einen anderen Typ umwandelbar ist.
 *
 * @author ASDFGamer
 */
public class Convertible
{

    /**
     * Dies zeigt, ob ein String zu boolean konvertiert werden kann.
     *
     * @param wert Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toBoolean(String wert)
    {

        return toBoolean(wert, new String[]{"true"}, new String[]{"false"});
    }

    /**
     * Dies zeigt, ob ein String zu Boolean konvertiert werden kann.
     *
     * @param wert   Der String
     * @param wahr   Alle Werte die zu true konvertiert werden können.
     * @param falsch Alle Werte die zu false konvertiert werden können.
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toBoolean(String wert, String[] wahr, String[] falsch)
    {

        for (String wahrheitswert : wahr)
        {
            if (wahrheitswert.equalsIgnoreCase(wert))
            {
                return true;
            }
        }
        for (String wahrheitswert : falsch)
        {
            if (wahrheitswert.equalsIgnoreCase(wert))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dies zeigt, ob ein String zu Byte konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toByte(String zahl)
    {

        try
        {
            Byte.parseByte(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Dies zeigt, ob ein String zu Short konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toShort(String zahl)
    {

        try
        {
            Short.parseShort(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Dies zeigt, ob ein String zu Int konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toInt(String zahl)
    {

        try
        {
            Integer.parseInt(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Dies zeigt, ob ein String zu Long konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toLong(String zahl)
    {

        try
        {
            Long.parseLong(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Dies zeigt, ob ein String zu Float konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toFloat(String zahl)
    {

        try
        {
            Float.parseFloat(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * Dies zeigt, ob ein String zu Double konvertiert werden kann.
     *
     * @param zahl Der String
     * @return true, wenn möglich, sonst false.
     */
    static public boolean toDouble(String zahl)
    {

        try
        {
            Double.parseDouble(zahl);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }


}
