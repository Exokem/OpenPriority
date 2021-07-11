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
    private static final Set<Runnable> SPECIALIZED_REFRESH_APPLICATORS = new HashSet<>();

    public static String bind(String key, Consumer<String> applicator)
    {
        GLOBAL_REFRESH_APPLICATORS.put(key, applicator);

        return get(key);
    }

    public static void bind(Runnable applicator)
    {
        SPECIALIZED_REFRESH_APPLICATORS.add(applicator);
    }

    public static String get(String key)
    {
        if (Options.Debug.LOCALE_DEBUG.get() || localeVariant == null) return key;

        return localeVariant.bindings.getOrDefault(key, key);
    }

    public static void setLocaleVariantAndUpdate(Variant variant)
    {
        localeVariant = variant;
        Variant.importBindings(localeVariant);
    }

    public static void refreshIndices()
    {
        OpenPriority.updateLocale();
        for (String key : GLOBAL_REFRESH_APPLICATORS.keySet())
        {
            GLOBAL_REFRESH_APPLICATORS.get(key).accept(get(key));
        }

        SPECIALIZED_REFRESH_APPLICATORS.forEach(Runnable::run);
    }

    public enum Variant
    {
        EN_GB, EN_US, ES_ES;

        private final Map<String, String> bindings = new HashMap<>();
        private final Map<String, String> localeReverseBindings = new HashMap<>();

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

                            try
                            {
                                Variant variant = valueOfTranslationKey(key);
                                localeVariant.localeReverseBindings.put(primitive.getAsString(), key);
                            }

                            catch (Exception ignored) {}

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

        public String translationKey()
        {
            return name().toLowerCase().replaceAll("_", "-");
        }

        public Variant inverseRetrieve(String translated)
        {
            String key = localeReverseBindings.get(translated);

            if (key == null) return Variant.valueOfTranslationKey(translated);

            return Variant.valueOfTranslationKey(key);
        }

        public static Set<String> translationKeySet()
        {
            Set<String> keys = new HashSet<>();

            for (Variant variant : Variant.values())
            {
                String key = variant.translationKey();
                keys.add(Locale.get(key));
            }

            return keys;
        }

        public static JsonElement exportJson(Variant variant)
        {
            return new JsonPrimitive(variant.name());
        }
    }
}
