package xkv.api.responsive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import xkv.OPIO;
import xkv.api.importers.GeneralImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public final class Locale
{
    private static Variant localeVariant = Variant.EN_UK;

    public static String get(String key)
    {
        if (localeVariant == null) return key;

        return localeVariant.bindings.getOrDefault(key, key);
    }

    public static void configureLocale(String locale)
    {
        try
        {
            localeVariant = Variant.valueOf(locale.toUpperCase());
        }

        catch (IllegalArgumentException i)
        {
            OPIO.warnf("Registered locale variant not found for locale '%s', using default", locale);
            localeVariant = Variant.EN_UK;
        }

        Variant.importBindings(localeVariant);
    }

    public enum Variant
    {
        EN_UK;

        private final Map<String, String> bindings = new HashMap<>();

        private static final String header = "locale/";

        public static void importBindings(Variant variant)
        {
            GeneralImporter.genericImport(header, variant.name().toLowerCase(), ".json", Variant::parseLocaleJson);
        }

        private static void parseLocaleJson(File file)
        {
            JsonReader reader = null;

            try
            {
                reader = new JsonReader(new FileReader(file));
            }

            catch (FileNotFoundException e)
            {
                OPIO.warnf("File not found: %s", file.getPath());
                return;
            }

            JsonElement source = JsonParser.parseReader(reader);

            if (source.isJsonObject())
            {
                JsonObject index = source.getAsJsonObject();

                int indices = 0;

                for (String key : index.keySet())
                {
                    JsonElement element = index.get(key);

                    if (element.isJsonPrimitive())
                    {
                        JsonPrimitive primitive = element.getAsJsonPrimitive();

                        if (primitive.isString())
                        {
                            localeVariant.bindings.put(key, primitive.getAsString());
                            indices ++;
                        }
                    }
                }

                OPIO.inff("Found %d indices for locale '%s'", indices, localeVariant.name());
            }
        }
    }
}
