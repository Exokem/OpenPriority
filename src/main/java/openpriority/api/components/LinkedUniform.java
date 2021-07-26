package openpriority.api.components;

import javafx.scene.Node;
import javafx.scene.layout.Priority;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LinkedUniform<X extends Node, V extends ILinkedUniformDisplayable<X>> extends Uniform
{
    public LinkedUniform(Alignment alignment)
    {
        this.align(alignment);
    }

    private final Map<String, V> linkedIndex = new LinkedHashMap<>();
    private final List<V> orderedIndex = new LinkedList<>();

    private boolean modified = false;

    public LinkedUniform<X, V> insert(V value)
    {
        return insert(orderedIndex.size(), value);
    }

    public LinkedUniform<X, V> insert(int position, V value)
    {
        orderedIndex.add(position, value);
        modified = true;
        return this;
    }

    public LinkedUniform<X, V> remove(int position)
    {
        if (position < orderedIndex.size())
        {
            orderedIndex.remove(position);
            modified = true;
        }
        return this;
    }

    public LinkedUniform<X, V> remove(V value)
    {
        orderedIndex.remove(value);
        modified = true;
        return this;
    }

    public LinkedUniform<X, V> refresh()
    {
        if (!modified) return this;

        getChildren().clear();
        for (int ix = 0; ix < orderedIndex.size(); ix ++)
        {
            add(orderedIndex.get(ix).display(), alignment.column(ix), alignment.row(ix), Priority.ALWAYS);
        }

        modified = false;

        return this;
    }
}
