package openpriority.api.importers;

import openpriority.OPIO;
import openpriority.OpenPriority;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Consumer;

public class GeneralImporter
{
    private static final String EXTERNAL = "./data/";

    public static String convertURL(URL url) throws MalformedURLException
    {
        return new File(url.getPath()).toURI().toURL().toString();
    }

    public static boolean internalConvertedImport(String header, String file, String extension, Consumer<String> applicator)
    {
        URL url = OpenPriority.class.getClassLoader().getResource(header + file + extension);

        try
        {
            if (url != null)
            {
                OPIO.inff("Loading stylesheet: " + url.getPath());

                String converted = convertURL(url);

                applicator.accept(converted);

                return true;
            }
        }

        catch (MalformedURLException ignored)
        {
            OPIO.warnf("Failed to convert URL '%s' while importing", url.getPath());
        }

        return false;
    }

    public static boolean internalGenericImport(String header, String file, String extension, Consumer<URL> applicator)
    {
        URL url = OpenPriority.class.getClassLoader().getResource(header + file + extension);

        if (url != null)
        {
            OPIO.inff("Loading stylesheet: " + url.getPath());

            applicator.accept(url);

            return true;
        }

        return false;
    }

    public static boolean convertedImport(String header, String name, String extension, Consumer<String> applicator)
    {
        File file = new File(EXTERNAL + header + name + extension);
        String path = file.getPath();

        try
        {
            file = file.getCanonicalFile();
            path = file.getCanonicalPath();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (file.exists())
        {
            OPIO.inff("Importing file: " + path);

            try { applicator.accept(file.toURI().toURL().toString()); }

            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }

    public static boolean genericImport(String header, String name, String extension, Consumer<File> applicator)
    {
        File file = new File(EXTERNAL + header + name + extension);

        if (file.exists())
        {
            try
            {
                OPIO.inff("Importing file: " + file.getCanonicalPath());

                applicator.accept(file);

                return true;
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }
}
