package org.asdfgamer.utils.file;

import org.asdfgamer.utils.other.OSValidator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.logging.Logger.getLogger;


/**
 * Sinnvolle Methoden für das Arbeiten mit lokalen Dateien.
 *
 * @author ASDFGamer
 */
public class FileUtil
{

    /**
     * Dasselbe wie System.getProperty("file.separator") nur kürzer (und vllt. schneller).
     */
    static public final String SEPERATOR = System.getProperty("file.separator");

    private final static Logger LOG = getLogger(FileUtil.class.getName());

    /**
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine Datei zeigt.
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
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine Datei zeigt.
     *
     * @param path Der Pfad zur Datei als File.
     * @return true, falls es eine Datei ist, sonnst false.
     */
    public static boolean isFile(File path)
    {

        return (path.exists() && !path.isDirectory());
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu einder Datei wirklich auf eine Datei zeigt.
     *
     * @param path Der Pfad zur Datei als Path.
     * @return true, falls es eine Datei ist, sonnst false.
     */
    public static boolean isFile(Path path)
    {

        return isFile(path.toFile());
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen Ordner zeigt.
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
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen Ordner zeigt.
     *
     * @param path Der Pfad zum Ordner als File.
     * @return true, falls es ein Ordner ist, sonnst false.
     */
    public static boolean isFolder(File path)
    {

        return (path.exists() && path.isDirectory());
    }

    /**
     * Diese Methode überprüft, ob ein Pfad zu eindem Ordner wirklich auf einen Ordner zeigt.
     *
     * @param path Der Pfad zum Ordner als Path.
     * @return true, falls es ein Ordner ist, sonnst false.
     */
    public static boolean isFolder(Path path)
    {

        return isFolder(path.toFile());
    }

    /**
     * Dies gibt einen sinnvollen Config Ordner zurück je nach OS.
     *
     * @param name Der Name des Ordners (Programms).
     * @return Den Pfad des Ordners.
     */
    public static String getConfigFolder(String name)
    {

        switch (OSValidator.getOSString())
        {
            case "windows":
                return getConfigFolderWin(name);

            case "linux":
                return getConfigFolderLinux(name);

            default:
                return getConfigFolderNormal(name);

        }

    }

    /**
     * Dies gibt einen guten Pfad für Windows zurück.
     *
     * @param name Der Name des Ordners (Programms).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderWin(String name)
    {

        String userpath = System.getProperty("user.home");
        userpath += SEPERATOR + "AppData" + SEPERATOR + "Roaming" + SEPERATOR + name + SEPERATOR;
        return userpath;
    }

    /**
     * Dies gibt einen guten Pfad für Linux zurück.
     *
     * @param name Der Name des Ordners (Programms).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderLinux(String name)
    {
        //TODO Den Ordner für Linux herausfinden.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Dies gibt einen ordentlichen Pfad für alle anderen OS zurück.
     *
     * @param name Der Name des Ordners (Programms).
     * @return Den Pfad des Ordners.
     */
    private static String getConfigFolderNormal(String name)
    {

        return System.getProperty("user.home") + SEPERATOR + name + SEPERATOR;
    }

    /**
     * Dies gibt eine Configfile zurück.
     *
     * @param progname Der Name des Programms ({@link FileUtil#getConfigFolder(java.lang.String) }).
     * @param filename Der Name der Logfile.
     * @return Den Pfad der Configfile.
     */
    public static String getConfigFile(String progname, String filename)
    {

        String sfile = /*"E:\\test\\";//*/getConfigFolder(progname) + filename;
        Path file = Paths.get(sfile);
        if (!FileUtil.isFile(file))
        {
            createFile(file);
        }
        return sfile;
    }

    /**
     * Dies erstellt die datei und wenn nötig auch jeden übergeordneten Ordner.
     *
     * @param datei Die Datei die erstellt werden soll als Pfad.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean createFile(String datei)
    {

        return createFile(Paths.get(datei));
    }

    /**
     * Dies erstellt die datei und wenn nötig auch jeden übergeordneten Ordner.
     *
     * @param datei Die Datei die erstellt werden soll.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean createFile(Path datei)
    {

        if (isFile(datei))
        {
            LOG.info("Die Datei " + datei.toString() + " exitert schon und muss nicht erstellt werden.");
            return true;
        }
        if (datei.getRoot() == null)
        {
            LOG.warning("Die angegebene Datei hat keine Root komponente und kann deshalb nicht erstellt werden.");
            return false;
        }
        if (!isFolder(datei.getParent()))
        {
            LOG.info("Der übergeordnete Ordner " + datei.getParent().toString() + " existert nicht und wird jetzt erstellt.");
            createFolder(datei.getParent());
        }

        try
        {
            Files.createFile(datei);
        } catch (IOException ex)
        {
            LOG.severe("Das erstellen der Datei " + datei.toString() + " hat nicht geklappt.");
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
            LOG.info("Der Ordner " + ordner.toString() + " existiert schon und muss nicht erstellt werden.");
            return true;
        }
        if (ordner.getRoot() == null)
        {
            LOG.warning("Die angegebene Datei hat keine Root komponente und kann deshalb nicht erstellt werden: " + ordner.toString());
            return false;
        }
        if (!isFolder(ordner.getParent()))
        {
            LOG.info("Der übergeordnete Ordner " + ordner.getParent().toString() + " existert nicht und wird jetzt erstellt.");
            createFolder(ordner.getParent());
        }

        try
        {
            Files.createDirectory(ordner);
        } catch (IOException ex)
        {
            LOG.severe("Das erstellen des Ordners " + ordner.toString() + " hat nicht geklappt.");
            return false;
        }
        return true;
    }

    /**
     * Dies kopiert eine Datei
     *
     * @param quelldatei Die Quelldatei
     * @param zieldatei  Die Zieldatei
     * @param flag       Die Einstellungen von {@link CopyOption}
     * @return true, falls das kopieren geklappt hat, sonst false
     */
    public static boolean copyFile(Path quelldatei, Path zieldatei, CopyOption[] flag)
    {

        try
        {
            if (!FileUtil.isFolder(Paths.get(zieldatei.getRoot().toString() + zieldatei.subpath(0, zieldatei.getNameCount() - 1).toString())))
            {
                FileUtil.createFolder(Paths.get(zieldatei.getRoot().toString() + zieldatei.subpath(0, zieldatei.getNameCount() - 1).toString()));
            }
            Files.copy(quelldatei, zieldatei, flag);
        } catch (IOException ex)
        {
            LOG.severe("Die Datei: " + quelldatei.toString() + " konnte nicht kopiert werden");
            return false;
        }
        return true;
    }

    /**
     * Dies schreibt eine Liste in eine Datei.
     *
     * @param <T>   jedes Objekt das zu einem String umgewandelt werden kann mit .toString().
     * @param liste Die Liste die gespeichtert werden soll.
     * @param datei Die Datei in die geschrieben werden soll.
     * @return true, falls es hingehauen hat, sonst false.
     */
    public static <T> boolean print(List<T> liste, Path datei)
    {

        if (!createFile(datei))
        {
            LOG.warning("Die Datei " + datei.toString() + " konnte nicht erstellt werden.");
            return false;
        }

        try (FileWriter writer = new FileWriter(datei.toFile()))
        {
            while (!liste.isEmpty())
            {
                writer.write(liste.remove(0).toString() + System.getProperty("line.separator"));
            }
        } catch (IOException ex)
        {
            LOG.warning("Die Datei " + datei.toString() + " konnte nicht geöffnet werden.");
        }
        return true;
    }

    /**
     * Dies erstellt alte versionen von einer Datei, falls möglich, wobei maximal  max_versionen Versionen erstellt werden.
     *
     * @param pfad        Der Pfad zu der Datei von der eine weitere Version angelegt werden soll
     * @param version     Die Versionsnummer von der Version die jetzt angelegt werden soll.
     * @param max_version Die maximale Anzahl an Versionen die erstellt werden können.
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean versionierung(Path pfad, int version, int max_version)
    {

        boolean result = true;
        if (max_version >= version)
        {
            if (version == 1)
            {
                LOG.fine("Es werden die alten Version von " + pfad.toString() + " verschoben/gelöscht");
            }
            if (FileUtil.isFile(pfad))
            {
                Path newpfad;
                if (version > 1)
                {
                    newpfad = Paths.get(pfad.toString().substring(0, pfad.toString().length() - 1) + version);
                } else
                {
                    newpfad = Paths.get(pfad.toString() + ".1");
                }
                result = versionierung(newpfad, version + 1, max_version);
                try
                {
                    Files.move(pfad, newpfad, REPLACE_EXISTING);
                } catch (IOException ex)
                {
                    LOG.warning("Es konnte " + pfad.toString() + " nicht umbenannt werden");
                    return false;
                }
            }
            return result;
        } else
        {
            LOG.fine("Es werden keine verschiedenen Versionen gespeichert oder es ist das versionslimit erreicht.");
            return true;
        }
    }


}
