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
import java.util.function.Consumer;
import java.util.function.Supplier;

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

        importIfNotnull(() -> options.getAsJsonObject(General.KEY), General::importOptions);
        importIfNotnull(() -> options.getAsJsonObject(Interface.KEY), Interface::importOptions);
        importIfNotnull(() -> options.getAsJsonObject(Debug.KEY), Debug::importOptions);
    }

    private static void importIfNotnull(Supplier<JsonObject> source, Consumer<JsonObject> destination)
    {
        JsonObject value = source.get();

        if (value != null)
        {
            destination.accept(value);
        }
    }

    public static void exportOptions(JsonObject container)
    {
        JsonObject options = new JsonObject();
        General.exportOptions(options);
        Interface.exportOptions(options);
        Debug.exportOptions(options);

        container.add(KEY, options);
    }

    private static void importBoolean(JsonObject source, Option<Boolean> destination)
    {
        if (source.has(destination.key())) destination.set(source.get(destination.key()).getAsBoolean());
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

    public static final class Interface
    {
        public static final String KEY = "options_interface";

        public static Option<Boolean> SHOW_INFORMATION = new Option<>("show_information", true, JsonPrimitive::new);
        public static Option<Boolean> SHOW_TIME = new Option<>("show_time", true, JsonPrimitive::new);

        public static void importOptions(JsonObject source)
        {
            importBoolean(source, SHOW_INFORMATION);
            importBoolean(source, SHOW_TIME);
        }

        public static void exportOptions(JsonObject container)
        {
            JsonObject interfaceOptions = new JsonObject();
            SHOW_TIME.exportJson(interfaceOptions);
            SHOW_INFORMATION.exportJson(interfaceOptions);

            container.add(KEY, interfaceOptions);
        }
    }

    public static final class Debug
    {
        public static final String KEY = "options_debug";

        public static Option<Boolean> LOCALE_DEBUG = new Option<>("locale_debug", false, JsonPrimitive::new);

        public static void importOptions(JsonObject source)
        {
            importBoolean(source, LOCALE_DEBUG);
        }

        public static void exportOptions(JsonObject container)
        {
            JsonObject debugOptions = new JsonObject();
            LOCALE_DEBUG.exportJson(debugOptions);

            container.add(KEY, debugOptions);
        }
    }

}
