package openpriority.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import openpriority.OPIO;
import openpriority.api.responsive.Locale;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public final class Options
{
    public static final String KEY = "options";

    public static void receiveJson(File jsonFile)
    {
        try
        {
            JsonElement json = JsonParser.parseReader(new FileReader(jsonFile));

            if (json.isJsonObject())
            {
                JsonObject object = json.getAsJsonObject();

                if (object.has(KEY))
                {
                    importOptions(object);
                }
            }
        }

        catch (FileNotFoundException e)
        {
            OPIO.warnf("Could not locate file while attempting to receive options data: %s", e.getLocalizedMessage());
        }
    }

    public static void importOptions(JsonObject source)
    {
        JsonObject options = source.getAsJsonObject(KEY);
        General.importOptions(options.getAsJsonObject(General.KEY));
        Debug.importOptions(options.getAsJsonObject(Debug.KEY));
    }

    public static void exportOptions(JsonObject container)
    {
        JsonObject options = new JsonObject();
        General.exportOptions(options);
        Debug.exportOptions(options);

        container.add(KEY, options);
    }

    public static final class General
    {
        public static final String KEY = "options_general";

        public static Option<Locale.Variant> ACTIVE_LOCALE = new Option<>("active_locale", Locale.Variant.EN_US, Locale.Variant::exportJson);

        public static void importOptions(JsonObject source)
        {
            if (source.has(ACTIVE_LOCALE.key()))
            {
                ACTIVE_LOCALE.set(Locale.Variant.valueOfTranslationKey(source.get(ACTIVE_LOCALE.key()).getAsString()));
            }
        }

        public static void exportOptions(JsonObject container)
        {
            JsonObject generalOptions = new JsonObject();
            ACTIVE_LOCALE.exportJson(generalOptions);

            container.add(KEY, generalOptions);
        }
    }

    public static final class Debug
    {
        public static final String KEY = "options_debug";

        public static Option<Boolean> LOCALE_DEBUG = new Option<>("locale_debug", false, bool -> new JsonPrimitive(bool.toString()));

        public static void importOptions(JsonObject source)
        {
            if (source.has(LOCALE_DEBUG.key()))
            {
                LOCALE_DEBUG.set(source.get(LOCALE_DEBUG.key()).getAsBoolean());
            }
        }

        public static void exportOptions(JsonObject container)
        {
            JsonObject debugOptions = new JsonObject();
            LOCALE_DEBUG.exportJson(debugOptions);

            container.add(KEY, debugOptions);
        }
    }

}
