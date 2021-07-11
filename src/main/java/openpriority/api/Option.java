package openpriority.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Consumer;
import java.util.function.Function;

public class Option<V>
{
    private final String key;
    private V value;
    private final Function<V, JsonElement> exporter;

    public Option(String key, V value, Function<V, JsonElement> exporter)
    {
        this.key = key;
        this.value = value;
        this.exporter = exporter;
    }

    public void set(V value)
    {
        this.value = value;
    }

    public V get()
    {
        return value;
    }

    public String key()
    {
        return key;
    }

    public void exportJson(JsonObject container)
    {
        container.add(key, exporter.apply(value));
    }
}
