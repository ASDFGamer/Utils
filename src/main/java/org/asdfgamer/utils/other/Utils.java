package org.asdfgamer.utils.other;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Alle mögliche sinnvollen Methoden, die mehrmals verwendet werden können.
 *
 * @author ASDFGamer
 */
@SuppressWarnings({"unused", "UnusedReturnValue", "WeakerAccess"})
public class Utils
{

    /**
     * Dasselbe wie System.getProperty("file.separator") nur kürzer.
     */
    static public final String SEPERATOR = System.getProperty("file.separator");

    /**
     * Der benutzte Logger.
     */
    private static final Logger LOG = Logger.getLogger(Utils.class.getName());

    private static final double MAX_ABWEICHUNG = 0.001;

    private static final String[] TRUE_VALUES = {"true", "wahr"};

    private static final String BETRIEBSSYSTEM = System.getProperty("os.name").toLowerCase();

    /**
     * Dies gibt 'true' zurück, wenn der gegebene String mit einem der gegebenen
     * Werte übereinstimmt.
     *
     * @param wert        Der String der überprüft werden soll.
     * @param true_values Die Strings die wahr sind.
     * @return true, falls der String gleich ist wie einer der true_values.
     */
    public static boolean isTrue(String wert, String[] true_values)
    {

        for (String wahr : true_values)
        {
            if (wahr.equalsIgnoreCase(wert))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dies gibt 'false' zurück, wenn der gegebene String mit einem der
     * gegebenen Werte übereinstimmt.
     *
     * @param wert         Der String der überprüft werden soll.
     * @param false_values Die Strings die falsch sind.
     * @return true, falls der String gleich ist wie einer der false_values.
     */
    public static boolean isFalse(String wert, String[] false_values)
    {

        for (String falsch : false_values)
        {
            if (falsch.equalsIgnoreCase(wert))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dies gibt einen sinnvollen Config-Ordner zurück je nach Betriebssystem.
     *
     * @param name Der Name des Ordners (sinnvollerweise der Programmname).
     * @return Den Pfad des Ordners.
     */
    public static String getConfigFolder(String name)
    {

        switch (getBetriebssystemOhneNull())
        {
            case "windows":
                return getConfigFolderWin(name);
            case "unix":
                return getConfigFolderLinux(name);
            case "macos":
                return getConfigFolderMacOS(name);
            default:
                return getConfigFolderNormal(name);
        }
    }

    public static boolean isTrue(String wert)
    {

        for (String wahr : TRUE_VALUES)
        {
            if (wahr.equalsIgnoreCase(wert))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Dies gibt den Pfad zu der gesuchten Einstellungsdatei zurück und erstellt eine leere Datei an der Stelle.
     *
     * @param progname Der Name des Programms ({@link Utils#getConfigFolder(java.lang.String)
     *                 }).
     * @param filename Der Name der Einstellungsdatei.
     * @return Den Pfad der Einstellungsdatei oder null falls sie nicht erstellt werden konnte.
     */
    public static String getConfigFile(String progname, String filename)
    {

        String sfile = getConfigFolder(progname) + filename;
        Path file = Paths.get(sfile);
        if (!isFile(file))
        {
            if (!createFile(file))
            {
                LOG.warning("Die Configdatei " + sfile + " konnte nicht erstellt werden.");
                return null;
            }
        }
        return sfile;
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine
     * Datei zeigt.
     *
     * @param path Der Pfad zur Datei als String.
     * @return true, falls es eine Datei ist, sonnst false.
     */
    public static boolean isFile(String path)
    {

        File file;
        try
        {
            file = new File(path);
        } catch (NullPointerException e)
        {
            return false;
        }
        return isFile(file);
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine
     * Datei zeigt.
     *
     * @param path Der Pfad zur Datei als File.
     * @return true, falls es eine Datei ist, sonnst false.
     */
    public static boolean isFile(File path)
    {

        return (path.exists() && !path.isDirectory());
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine
     * Datei zeigt.
     *
     * @param path Der Pfad zur Datei als Path.
     * @return true, falls es eine Datei ist, sonnst false.
     */
    public static boolean isFile(Path path)
    {

        return isFile(path.toFile());
    }

    /**
     * Dies erstellt die Datei und wenn nötig auch jeden übergeordneten Ordner.
     *
     * @param datei Die Datei die erstellt werden soll als Pfad.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean createFile(String datei)
    {

        return createFile(Paths.get(datei));
    }

    /**
     * Dies erstellt die Datei und wenn nötig auch jeden übergeordneten Ordner.
     *
     * @param datei Die Datei die erstellt werden soll.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean createFile(Path datei)
    {

        if (isFile(datei))
        {
            LOG.log(Level.INFO, "Die Datei {0} exitert schon und muss nicht erstellt werden.", datei.toString());
            return true;
        }
        if (datei.getRoot() == null)
        {
            LOG.warning("Die angegebene Datei hat keine Root komponente und kann deshalb nicht erstellt werden.");
            return false;
        }
        if (!isFolder(datei.getParent()))
        {
            LOG.log(Level.INFO, "Der \u00fcbergeordnete Ordner {0} existert nicht und wird jetzt erstellt.", datei.getParent().toString());
            createFolder(datei.getParent());
        }

        try
        {
            Files.createFile(datei);
        } catch (IOException ex)
        {
            LOG.log(Level.WARNING, "Das erstellen der Datei {0} hat nicht geklappt.", datei.toString());
            return false;
        }
        return true;
    }

    /**
     * Dies erstellt den Ordner und wenn nötig auch jeden übergeordneten Ordner.
     *
     * @param ordner Der Ordner der erstellt werden soll.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean createFolder(Path ordner)
    {

        if (isFolder(ordner))
        {
            LOG.log(Level.INFO, "Der Ordner {0} existiert schon und muss nicht erstellt werden.", ordner.toString());
            return true;
        }
        if (isFile(ordner))
        {
            LOG.warning("Am Pfad des Ordners existiert eine Datei mit gleichem Namen, deshalb kann kein Ordner erstellt werden: " + ordner.toString());
            return false;
        }
        if (ordner.getRoot() == null)
        {
            LOG.log(Level.WARNING, "Die angegebene Datei hat keine Root Komponente und kann deshalb nicht erstellt werden: {0}", ordner.toString());
            return false;
        }
        if (!isFolder(ordner.getParent()))
        {
            LOG.log(Level.INFO, "Der \u00fcbergeordnete Ordner {0} existert nicht und wird jetzt erstellt.", ordner.getParent().toString());
            createFolder(ordner.getParent());
        }

        try
        {
            Files.createDirectory(ordner);
        } catch (IOException ex)
        {
            LOG.log(Level.WARNING, "Das erstellen des Ordners {0} hat nicht geklappt.", ordner.toString());
            return false;
        }
        return true;
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen
     * Ordner zeigt.
     *
     * @param path Der Pfad zum Ordner als String.
     * @return true, falls es ein Ordner ist, sonnst false.
     */
    public static boolean isFolder(String path)
    {

        File folder;
        try
        {
            folder = new File(path);
        } catch (NullPointerException e)
        {
            return false;
        }
        return isFolder(folder);
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen
     * Ordner zeigt.
     *
     * @param path Der Pfad zum Ordner als File.
     * @return true, falls es ein Ordner ist, sonnst false.
     */
    public static boolean isFolder(File path)
    {

        return (path.exists() && path.isDirectory());
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen
     * Ordner zeigt.
     *
     * @param path Der Pfad zum Ordner als Path.
     * @return true, falls es ein Ordner ist, sonnst false.
     */
    public static boolean isFolder(Path path)
    {

        return isFolder(path.toFile());
    }

    /**
     * Dies gibt das Betriebssystem welches genutzt wird als String aus.
     * <p>
     * Dies baut auf {@link Utils#getBetriebssystem()} auf, nur dass null zu dem leeren String umgewandelt wird.
     *
     * @return windows, linux, macos, solaris, ""(unbekannt)
     */
    public static String getBetriebssystemOhneNull()
    {
        String os = getBetriebssystem();
        if (os == null)
        {
            return "";
        }
        return os;
    }

    /**
     * Dies gibt das Betriebssystem welches genutzt wird als String aus.
     *
     * @return windows, linux, macos, solaris, null(unbekannt)
     */
    public static String getBetriebssystem()
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
     * Gibt an ob es sich bei dem Betriebssystem um ein Windows handelt.
     *
     * @return true, falls Windows, sonst false.
     */
    public static boolean isWindows()
    {

        return (BETRIEBSSYSTEM.contains("win"));

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um ein Unix/Linux handelt.
     *
     * @return true, falls Unix/Linux, sonst false.
     */
    public static boolean isUnix()
    {

        return (BETRIEBSSYSTEM.contains("nix") || BETRIEBSSYSTEM.contains("nux") || BETRIEBSSYSTEM.indexOf("aix") > 0);

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um ein Mac handelt.
     *
     * @return true, falls Mac, sonst false.
     */
    public static boolean isMac()
    {

        return (BETRIEBSSYSTEM.contains("mac"));

    }

    /**
     * Gibt an ob es sich bei dem Betriebssystem um Solaris handelt.
     *
     * @return true, falls Solaris, sonst false.
     */
    public static boolean isSolaris()
    {

        return (BETRIEBSSYSTEM.contains("sunos"));

    }

    /**
     * Diese Funktion filtert die Argumente aus einem String. Bsp: (2,516,3)
     * wird zu [2,516,3].
     *
     * @param text Der Text der geparst werden soll.
     * @return Ein Array aus den int Argumenten die geparst wurden.
     */
    public static int[] getArgs(String text)
    {

        int[] args = new int[(int) (text.length() / 2 + 0.5)];
        int argpos = 0;
        String zahl = "";
        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c >= '0' && c <= '9')
            {
                zahl += c;
            } else if (!zahl.isEmpty())
            {
                try
                {
                    args[argpos] = Integer.parseInt(zahl);
                    argpos++;
                } catch (NumberFormatException e)
                {
                    LOG.log(Level.WARNING, "Die Zahl {0} ist zu gro\u00df und darf darum nicht benutzt werden.", zahl);
                }
                zahl = "";

            }
        }
        int[] argskurz = new int[argpos];
        System.arraycopy(args, 0, argskurz, 0, argpos);
        return argskurz;
    }

    /**
     * Hiermit wird der Sinus in Grad für den angegebenen Winkel berechnet.
     *
     * @param winkel Der Winkel von 0 bis 360 Grad.
     * @return Der zugehörige Sinus.
     */
    public static double sinRad(double winkel)
    {

        double sin = Math.sin(winkel * 2 * Math.PI / 360);
        if (Math.abs(sin) < MAX_ABWEICHUNG)
        {
            return 0;
        }
        return sin;
    }

    /**
     * Hiermit wird der Kosinus in Grad für den angegebenen Winkel berechnet.
     *
     * @param winkel Der Winkel von 0 bis 360 Grad.
     * @return Der zugehörige Cosinus.
     */
    public static double cosRad(double winkel)
    {

        double cos = Math.cos(winkel * 2 * Math.PI / 360);
        if (Math.abs(cos) < MAX_ABWEICHUNG)
        {
            return 0;
        }
        return cos;
    }

    /**
     * Hiermit wird der Arctangens für die beiden längen berechnet. Die Argumente dürfen auch negativ sein. Die Ausgabe ist in Grad.
     *
     * @param y Die länge in y-Richtung.
     * @param x Die länge in x-Richtung.
     * @return Der Winkel von 0 bis 360.
     */
    public static double arctan(double y, double x)
    {

        double tan = Math.atan2(y, x) * 360 / (2 * Math.PI);
        if (Math.abs(tan) < MAX_ABWEICHUNG)
        {
            return 0;
        }
        return tan;
    }

    /**
     * Hiermit werden alle Felder von einem Typ aus einem Objekt ausgelesen und dann die Felder die zu einem bestimmten
     * Typ gehören als List zurückgegeben.
     *
     * @param klasse Dies ist das Objekt aus dem die Felder ausgelesen werden. Es kann sich auch um das dazugehörige
     *               Class-Objekt handeln, aber dann können nur statische Felder betrachtet werden.
     * @param <T>    Dies ist der Typ von dem die ausgelesenen Felder sein sollen.
     * @return Dies ist eine Liste mit allen Feldern des angegebenen Typs.
     * Todo aufräumen, testen, falls ein String übergeben wird und nach SettingPropertys gescht wird treten anscheinend fehler auf.
     */
    public static <T> Map<String, T> getFields(Object klasse)
    {
        if (isEnum(klasse))
        {
            LOG.warning("getFields doesn't work with Enums, because it adds Enum Fields to the Map even if they are not from Type T");//TODO ask StackOverflow
            return new HashMap<>();
        }
        Field[] fields;
        if (klasse instanceof Class)
        {
            fields = ((Class) klasse).getDeclaredFields();
        } else
        {
            fields = klasse.getClass().getDeclaredFields();
        }
        LOG.fine("Klasse: " + klasse.getClass().getName() + " hat " + fields.length + " Felder");
        Map<String, T> felder = new HashMap<>();
        for (Field field : fields)
        {
            try
            {
                LOG.fine("is Public " + Modifier.isPublic(field.getModifiers()));
                if (Modifier.isPublic(field.getModifiers()))
                {

                    if (klasse instanceof Class)
                    {
                        if (Modifier.isStatic(field.getModifiers()))//Da keine Instanz der Klasse da ist können nur sstatische Felder betrachtet werden.
                        {
                            try
                            {

                                // Ist nicht zu vermeiden, da ein vorheriger Typecheck mit einem Generic nicht möglich ist.
                                //noinspection unchecked
                                felder.put(field.getName(), (T) field.get(null));
                            } catch (NullPointerException e)
                            {
                                if (!((Class) klasse).isEnum())//Damit die Meldung nicht zu oft ausgegeben wird, da es dort nicht verhindert werden kann.
                                {
                                    LOG.fine("Wenn zum Abfragen der Felder einer Klasse nur ein Class-Objekt übergeben " +
                                            "wird, dann können nur statische Felder betrachtet werden (bei der Klasse "
                                            + ((Class) klasse).getSimpleName() + ").");//Falls ein Weg gefunden werden kann dies nicht auszugeben, falls T instanceof SettingsProperty, dann kann es wieder zu Info werden, aber ansonsten ist es zu oft ausgegeben.
                                }
                            } catch (Exception e)
                            {
                                LOG.fine("Unbekanntes Problem in getFields");
                            }
                        }

                    } else
                    {
                        //noinspection unchecked Ist nicht zu vermeiden, da ein vorheriger Typecheck mit einem Generic nicht möglich ist.
                        felder.put(field.getName(), (T) field.get(klasse));
                    }
                }
            } catch (IllegalAccessException e)
            {
                LOG.info("Das Feld " + field.getName() + " kann nicht ausgelesen werden.");
                e.printStackTrace();
            } catch (ClassCastException e)
            {
                LOG.info("Es ist kein Cast von " + field.getName() + "möglich");
            }
        }
        return felder;
    }

    /**
     * This tests if the given object is an Enum, or an Class object of an Enum or a String that represents an Enum.
     *
     * @param enumObject The Object that should be checked.
     * @return true, if it ist an enum, otherwise false.
     */
    public static boolean isEnum(Object enumObject)
    {
        if (enumObject instanceof String)
        {
            try
            {
                return Class.forName((String) enumObject).isEnum();
            } catch (ClassNotFoundException e)
            {
                return false;
            }
        } else if (enumObject instanceof Class)
        {
            return ((Class) enumObject).isEnum();
        } else
        {
            return enumObject.getClass().isEnum();
        }
    }

    /**
     * Diese Methode gibt ein Enumelement zurück, welches durch einen String angegeben wurde. Dieser String muss der
     * gesamte Name sein, z.B. java.lang.annotation.ElementType.ANNOTATION_TYPE
     *
     * @param enumElement Der String der das Enumelement angibt.
     * @return Entweder das Enumelement oder null, falls es nicht gefunden werden konnte.
     */
    public static Enum getEnumElement(String enumElement)
    {
        String classname = enumElement.substring(0, enumElement.lastIndexOf("."));
        String elementName = enumElement.substring(enumElement.lastIndexOf(".") + 1);
        try
        {
            Class classelement = Class.forName(classname);
            if (classelement.isEnum())
            {
                @SuppressWarnings("unchecked") Class<Enum> enumclass = (Class<Enum>) classelement;
                Enum[] enumConstants = enumclass.getEnumConstants();
                for (Enum enumConstant : enumConstants)
                {
                    if (classelement.isInstance(enumConstant) && enumConstant.name().equals(elementName))
                    {
                        return enumConstant;
                    }
                }
            }
        } catch (ClassNotFoundException e)
        {
            LOG.warning("Die im String angegebene Klasse " + classname + " existiert nicht.");
            return null;
        }
        LOG.warning("das element " + elementName + " wurde nicht in " + classname + " gefunden.");
        return null;
    }

    /**
     * Dies gibt einen guten Pfad für Windows zurück. Hierbei wird davon
     * ausgegangen das mindestens Win Vista eingesetzt wird, da der Ordner in
     * 'User\AppData\Roaming' liegt.
     *
     * @param name Der Name des Ordners (sinnvollerweise der Programmname).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderWin(String name)
    {

        String userpath = System.getProperty("user.home");
        userpath += SEPERATOR + "AppData" + SEPERATOR + "Roaming" + SEPERATOR + name + SEPERATOR;
        return userpath;
    }

    /**
     * Dies gibt einen guten Pfad für Linux zurück. Dieser Pfad ligt in
     * '~\.config'.
     *
     * @param name Der Name des Ordners (sinnvollerweise der Programmname).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderLinux(String name)
    {

        String userpath = System.getProperty("user.home");
        userpath += SEPERATOR + ".config" + SEPERATOR + name + SEPERATOR;
        return userpath;
    }

    /**
     * Dies gibt einen guten Pfad für MacOS zurück.
     *
     * @param name Der Name des Ordners (sinnvollerweise der Programmname).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderMacOS(String name)
    {

        String userpath = System.getProperty("user.home");
        userpath += SEPERATOR + name + SEPERATOR;
        return userpath;
    }

    /**
     * Dies gibt einen ordentlichen Pfad für alle anderen Betriebssysteme
     * zurück.
     *
     * @param name Der Name des Ordners (sinnvollerweise der Programmname).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderNormal(String name)
    {

        return System.getProperty("user.home") + SEPERATOR + name + SEPERATOR;
    }
}
