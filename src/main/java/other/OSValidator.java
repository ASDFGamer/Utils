package other;

/**
 * Hiermit wird das Betriebssystem erkannt.
 * Original von: mkyong von https://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/
 * von mir abgeändert.
 */
public class OSValidator
{

    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Dies gibt das Betriebssystem welches genutzt wird als Integer aus.
     *
     * @return 1:Windows; 2:Unix/Linux; 3:MacOS; 4:Solaris; -1:Unbekannt
     */
    public static int getOSint()
    {

        if (isWindows())
        {
            return 1;
        } else if (isUnix())
        {
            return 2;
        } else if (isMac())
        {
            return 3;
        } else if (isSolaris())
        {
            return 4;
        } else
        {
            return -1;
        }
    }

    /**
     * Dies gibt das Betriebssystem welches genutzt wird als String aus.
     *
     * @return windows, linux, macos, solaris, null(unbekannt)
     */
    public static String getOSString()
    {

        if (isWindows())
        {
            return "windows";
        } else if (isUnix())
        {
            return "unix";
        } else if (isMac())
        {
            return "macos";
        } else if (isSolaris())
        {
            return "solaris";
        } else
        {
            return null;
        }
    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um ein Windows handelt.
     *
     * @return true, falls Windows, sonst false.
     */
    public static boolean isWindows()
    {

        return (OS.contains("win"));

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um ein Mac handelt.
     *
     * @return true, falls Mac, sonst false.
     */
    public static boolean isMac()
    {

        return (OS.contains("mac"));

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um ein Unix/Linux handelt.
     *
     * @return true, falls Unix/Linux, sonst false.
     */
    public static boolean isUnix()
    {

        return (OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0);

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um Solaris handelt.
     *
     * @return true, falls Solaris, sonst false.
     */
    public static boolean isSolaris()
    {

        return (OS.contains("sunos"));

    }
}