package org.asdfgamer.utils.file;

import org.asdfgamer.utils.other.Utils;

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
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileUtil
{

    /**
     * Dasselbe wie System.getProperty("file.separator") nur kürzer (und vllt. schneller).
     */
    static public final String SEPERATOR = System.getProperty("file.separator");

    private final static Logger LOG = getLogger(FileUtil.class.getName());

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
            if (!Utils.isFolder(Paths.get(zieldatei.getRoot().toString() + zieldatei.subpath(0, zieldatei.getNameCount() - 1).toString())))
            {
                Utils.createFolder(Paths.get(zieldatei.getRoot().toString() + zieldatei.subpath(0, zieldatei.getNameCount() - 1).toString()));
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

        if (!Utils.createFile(datei))
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
            if (Utils.isFile(pfad))
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
