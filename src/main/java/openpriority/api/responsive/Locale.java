package openpriority.api.responsive;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import openpriority.OPIO;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.importers.GeneralImporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public final class Locale
{
    private static Variant localeVariant = Variant.EN_GB;

    private static final Map<String, Consumer<String>> GLOBAL_REFRESH_APPLICATORS = new HashMap<>();

    public static String bind(String key, Consumer<String> applicator)
    {
        GLOBAL_REFRESH_APPLICATORS.put(key, applicator);

        return get(key);
    }

    public static String get(String key)
    {
        if (Options.Debug.LOCALE_DEBUG.get() || localeVariant == null) return key;

        return localeVariant.bindings.getOrDefault(key, key);
    }

    public static void refreshIndices()
    {
        OpenPriority.updateLocale();
        for (String key : GLOBAL_REFRESH_APPLICATORS.keySet())
        {
            GLOBAL_REFRESH_APPLICATORS.get(key).accept(get(key));
        }
    }

    public static void configureLocale(String locale)
    {
        try
        {
            localeVariant = Variant.valueOfTranslationKey(locale);
        }

        catch (IllegalArgumentException i)
        {
            OPIO.warnf("Registered locale variant not found for locale '%s', using default", locale);
            localeVariant = Variant.EN_GB;
        }

        Variant.importBindings(localeVariant);
    }

    public enum Variant
    {
        EN_GB, EN_US, ES_ES;

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

        public static Variant valueOfTranslationKey(String key)
        {
            return valueOf(key.replaceAll("-", "_").toUpperCase());
        }

        public static Set<String> translationKeySet()
        {
            Set<String> keys = new HashSet<>();

            for (Variant variant : Variant.values())
            {
                keys.add(variant.name().toLowerCase().replaceAll("_", "-"));
            }

            return keys;
        }

        public static JsonElement exportJson(Variant variant)
        {
            return new JsonPrimitive(variant.name());
        }
    }
}
