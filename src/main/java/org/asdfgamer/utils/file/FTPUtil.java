package org.asdfgamer.utils.file;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * Dies sind sinnvolle Funktionen für das Arbeiten mit FTP.
 *
 * @author ASDFGamer
 */
public class FTPUtil
{

    private final static Logger LOG = getLogger(FTPUtil.class.getName());

    /**
     * Dies überprüft ob eine Datei existiert.
     *
     * @param path   Der Pfad zu der Datei von dem aktuellen Ordner des Clients aus.
     * @param client Der Client bei dem der Pfad überprüft werden soll
     * @param stay   legt fest ob der client in dem zuletzt verwendeten Verzeichnis bleiben soll oder ob er zurück zum Anfang soll.
     * @return true, falls die Datei existiert, sonst false.
     */
    public static boolean isFileFTP(Path path, FTPClient client, boolean stay)
    {

        String dirnow = "";
        try
        {
            dirnow = client.printWorkingDirectory();
        } catch (IOException e)
        {
            LOG.info("Das aktuelle Verzeichnis konnte nicht ausgelesen werden.");
        }
        int i = -1;
        boolean result = true;
        if (path.getNameCount() > 1)
        {
            for (i = 0; i < path.getNameCount() - 1; i++)
            {
                if (!isFolderFTP(path.getName(i), client, true))
                {
                    return false;
                }
            }
        }
        try
        {
            InputStream inputStream = client.retrieveFileStream(path.getName(path.getNameCount() - 1).toString());
            int returnCode = client.getReplyCode();
            result = !(inputStream == null || returnCode == 550);

            if (!stay)
            {
                client.changeWorkingDirectory(dirnow);
            }
        } catch (IOException e)
        {
            result = false;
        }
        return result;
    }

    /**
     * Dies überprüft ob ein bestimmtes Verzeichnis existiert.
     *
     * @param path   Der Pfad zum Verzeichnis vom aktuellen Pfad des Clients aus.
     * @param client Der FTP-Client der überprüft werden soll.
     * @param stay   legt fest ob der client in dem zuletzt verwendeten Verzeichnis bleiben soll oder ob er zurück zum Anfang soll.
     * @return true, falls der Ordner existiert, sonst false.
     */
    public static boolean isFolderFTP(String path, FTPClient client, boolean stay)
    {

        return isFolderFTP(Paths.get(path), client, stay);
    }

    /**
     * Dies überprüft ob ein bestimmtes Verzeichnis existiert.
     *
     * @param path   Der Pfad zum Verzeichnis vom aktuellen Pfad des Clients aus.
     * @param client Der FTP-Client der überprüft werden soll.
     * @param stay   legt fest ob der client in dem zuletzt verwendeten Verzeichnis bleiben soll oder ob er zurück zum Anfang soll.
     * @return true, falls der Ordner existiert, sonst false.
     */
    public static boolean isFolderFTP(Path path, FTPClient client, boolean stay)
    {

        String dirnow = "";
        try
        {
            dirnow = client.printWorkingDirectory();
        } catch (IOException e)
        {
            LOG.info("Das aktuelle Verzeichnis konnte nicht ausgelesen werden.");
        }
        int i = -1;
        boolean result = true;
        /*if (path.getNameCount()>1)Warum ist dieser Code da?
        {
            for (i = 0; i<path.getNameCount()-1;i++)
            {
                if (!isFolderFTP(path.getName(i),client,true))
                {
                    return false;
                }
            }
        }*/
        try
        {
            client.changeWorkingDirectory(path.toString());
            int returnCode = client.getReplyCode();
            result = returnCode != 550;

            if (!stay)
            {
                client.changeWorkingDirectory(dirnow);
            }
        } catch (IOException e)
        {
            result = false;
        }
        return result;
    }

    /**
     * Dies Erstellt einen Ordner an dem angegbenen Pfad
     *
     * @param path   Der Ordnerpfad relativ zum aktuellen workingDirectory
     * @param client Der FTP-Client auf dem der Ordner erstellt werden soll
     * @return true, falls der Ordner erstellt werden konnte oder schon existiert, sonst false.
     */
    public static boolean createFolderFTP(Path path, FTPClient client)
    {

        String dirnow = "";
        try
        {
            dirnow = client.printWorkingDirectory();
        } catch (IOException e)
        {
            LOG.info("Das aktuelle Verzeichnis konnte nicht ausgelesen werden.");
        }

        if (isFolderFTP(path, client, false))
        {
            LOG.info("Der Ordner " + path + " existiert schon auf dem Client " + client.getRemoteAddress().getHostAddress());
            return true;
        }
        if (path.getNameCount() > 1)
        {
            LOG.info(path.toString() + " zu " + path.subpath(0, path.getNameCount() - 1).toString());
            if (!createFolderFTP_rec(path.subpath(0, path.getNameCount() - 1), client))
            {
                return false;
            }
        }
        try
        {
            client.makeDirectory(path.toString());
            client.changeWorkingDirectory(dirnow);
            LOG.info("Working Directory: " + client.printWorkingDirectory() + "create Dir");

        } catch (IOException ex)
        {
            LOG.warning("Das Verzeichnis " + path + " konnte nicht auf " + client.getRemoteAddress().getHostAddress() + " erstellt werden.");
            return false;
        }

        return true;
    }

    /**
     * Dies Erstellt einen Ordner an dem angegbenen Pfad
     *
     * @param path   Der Ordnerpfad relativ zum aktuellen workingDirectory
     * @param client Der FTP-Client auf dem der Ordner erstellt werden soll
     * @return true, falls der Ordner erstellt werden konnte, sonst false.
     */
    private static boolean createFolderFTP_rec(Path path, FTPClient client)
    {

        if (isFolderFTP(path, client, false))
        {
            LOG.info("Der Ordner " + path + " existiert schon auf dem Client " + client.getRemoteAddress().getHostAddress());
        }
        if (path.getNameCount() > 1)
        {
            LOG.info(path.toString() + " zu " + path.subpath(0, path.getNameCount() - 1).toString());
            if (!createFolderFTP_rec(path.subpath(0, path.getNameCount() - 1), client))
            {
                return false;
            }
        }
        try
        {
            client.makeDirectory(path.toString());

        } catch (IOException ex)
        {
            LOG.warning("Das Verzeichnis " + path + " konnte nicht auf " + client.getRemoteAddress().getHostAddress() + " erstellt werden.");
            return false;
        }

        return true;
    }

    /**
     * Dies kopiert eine Datei von dem lokalen quellpfad zu dem ftp pfad der relativ zum Arbeitsverzeichnis des clients ist.
     *
     * @param quelldatei Die Datei die kopiert werden soll.
     * @param zieldatei  Der Name der Datei die erstellt werden soll durch kopieren.
     * @param client     Der Client der mit dem FTP-SERver verbunden ist.
     *                   TODO Metainfos übernehmen besonders erstell und mod datum
     * @return true, falls alles hingehauen hat, sonst false.
     */
    public static boolean copyFileFTP(Path quelldatei, Path zieldatei, FTPClient client)
    {

        boolean result = true;
        String dirnow = "";
        try
        {
            dirnow = client.printWorkingDirectory();
        } catch (IOException e)
        {
            LOG.info("Das aktuelle Verzeichnis konnte nicht ausgelesen werden.");
        }
        try
        {
            if (zieldatei.getNameCount() > 1 && !isFolderFTP(zieldatei.subpath(0, zieldatei.getNameCount()), client, false))
            {
                LOG.info("Create Folder: " + zieldatei.subpath(0, zieldatei.getNameCount() - 1));
                createFolderFTP(zieldatei.subpath(0, zieldatei.getNameCount() - 1), client);
                client.changeWorkingDirectory(zieldatei.subpath(0, zieldatei.getNameCount()).toString());
            }
            FileInputStream fis = new FileInputStream(quelldatei.toFile());
            client.storeFile(zieldatei.toString(), fis);
            fis.close();
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply))
            {
                LOG.info(client.getReplyString());
                result = false;
            }
            client.changeWorkingDirectory(dirnow);
            LOG.info("Working Directory: " + client.printWorkingDirectory() + "copyFile");
        } catch (FileNotFoundException e)
        {
            LOG.severe("Die Datei die kopiert werden soll konte nicht gefunden werden.");
            return false;
        } catch (IOException e)
        {
            LOG.severe("Es gab ein Problem beim kopieren der Datei");
            return false;
        }
        return result;
    }

    /**
     * Dies gibt das änderungsdatum einer Datei an
     *
     * @param datei  Der relative Pfad zu der Datei
     * @param client Der Client der benutzt werden soll.
     * @return Das Datum wann die Datei das letzte mal verändert wurde oder null falls es ein problem gab.
     */
    public static Date getModificationDate(Path datei, FTPClient client)
    {

        String servertime;
        try
        {
            servertime = client.getModificationTime(datei.toString());
        } catch (IOException e)
        {
            LOG.info("Das letzte änderungsdatum konnte nicht ausgelesen werden.");
            return null;
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        try
        {
            String timePart = servertime.split(" ")[1];
            Date modificationTime = dateFormat.parse(timePart);
            System.out.println("File modification time: " + modificationTime);
            return modificationTime;
        } catch (ParseException ex)
        {
            LOG.warning("Das Datum konnte nicht gephrast werden.");
            return null;
        }
    }

    /**
     * Dies erstellt alte versionen von einer Datei, falls möglich, wobei maximal max_version Versionen erstellt werden.
     *
     * @param pfad        Der Pfad zu der Datei von der eine weitere Version angelegt werden soll
     * @param version     Die Versionsnummer von der Version die jetzt angelegt werden soll.
     * @param max_version Die maximale Anzahl an Versionen die erstellt werden können.
     * @param client      Der benutzte FTP-Client
     * @return true, falls es geklappt hat, sonst false.
     */
    public static boolean versionierungFTP(Path pfad, int version, int max_version, FTPClient client)
    {

        boolean result = true;
        if (max_version >= version)
        {
            if (version == 1)
            {
                LOG.fine("Es werden die alten Version von " + pfad.toString() + " verschoben/gelöscht");
            }
            if (isFileFTP(pfad, client, false))
            {
                Path newpfad;
                if (version > 1)
                {
                    newpfad = Paths.get(pfad.toString().substring(0, pfad.toString().length() - 1) + version);
                } else
                {
                    newpfad = Paths.get(pfad.toString() + ".1");
                }
                result = versionierungFTP(newpfad, version + 1, max_version, client);
                try
                {
                    client.rename(pfad.toString(), newpfad.toString());//Problem falls Pfad und nicht nur Dateiname?
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

    /**
     * Dies wechselt zu einem bestimmten Verzeichnis
     *
     * @param directory Das Verzeichnis zu dem gewechselt werden soll.
     * @param client    Der Client bei dem dies passieren soll.
     * @return true, falls es erfolgreich war, sonst false.
     * @deprecated Nicht benutzen, da client.changeWorkingDirectory(directory) ziemlich das gleiche macht, aber auf jeden fall ohne Bug ist.
     */
    @Deprecated
    public static boolean changeToDir(String directory, FTPClient client) //noch nicht getestet
    {

        boolean noError = true;
        boolean result = false;
        int reply;
        String tempdir = "";
        try
        {
            String dirnow = client.printWorkingDirectory();
            while (noError)
            {
                client.changeToParentDirectory();
                reply = client.getReplyCode();
                noError = FTPReply.isPositiveCompletion(reply) && !tempdir.equals(client.printWorkingDirectory());
                if (isFolderFTP(directory, client, false))
                {
                    client.changeWorkingDirectory(directory);
                    result = true;
                }
                tempdir = client.printWorkingDirectory();

            }
            if (!result)
            {
                client.changeWorkingDirectory(dirnow);
                return false;
            } else
            {
                return true;
            }
        } catch (IOException e)
        {
            return false;
        }
    }

    /**
     * Dies wechselt zu einem bestimmten absoluten (Root)Verzeichnis
     *
     * @param directory Das (Root)Verzeichnis zu dem gewechselt werden soll.
     * @param client    Der Client bei dem dies passieren soll.
     * @return true, falls es erfolgreich war, sonst false.
     * @deprecated Nicht benutzen, da client.changeWorkingDirectory(directory) ziemlich das gleiche macht, aber auf jeden fall ohne Bug ist.
     */
    @Deprecated
    public static boolean changeToDirROOT(String directory, FTPClient client)
    {

        boolean noError = true;
        boolean result = false;
        int reply;
        String tempdir = "";
        try
        {
            String dirnow = client.printWorkingDirectory();
            //tempdir = dirnow;
            System.out.println(dirnow);
            while (noError)
            {
                client.changeToParentDirectory();
                System.out.println(client.printWorkingDirectory());
                reply = client.getReplyCode();
                System.out.println(reply);
                noError = FTPReply.isPositiveCompletion(reply) && !tempdir.equals(client.printWorkingDirectory());
                System.out.println(noError);
                tempdir = client.printWorkingDirectory();
            }
            System.out.println(client.changeWorkingDirectory(directory));
            if (directory.equals(client.printWorkingDirectory()))
            {
                System.out.println("Es wurde in das richtige verzeichnis gewechselt.");
                result = true;
            }
            if (!result)
            {
                client.changeWorkingDirectory(dirnow);
                System.out.println("Es gab einen Fehler und es wird zurück in das anfangsverzeichis gewechselt.");
                return false;
            } else
            {
                return true;
            }
        } catch (IOException e)
        {
            return false;
        }
    }


}
