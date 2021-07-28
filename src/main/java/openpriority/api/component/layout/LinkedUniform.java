package openpriority.api.component.layout;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Priority;

import java.util.*;
import java.util.function.Consumer;

public class LinkedUniform<X extends Node, V extends ILinkedUniformDisplayable<X>> extends Uniform
{
    public LinkedUniform(Alignment alignment)
    {
        this.align(alignment);
    }

    private final Map<V, Integer> validationIndex = new HashMap<>();
    private final List<V> orderedIndex = new LinkedList<>();

    private boolean modified = false;

    private ObjectProperty<V> selectedObjectProperty = new SimpleObjectProperty<>(this, "selected_object", null);

    public LinkedUniform<X, V> insert(V value)
    {
        return insert(orderedIndex.size(), value);
    }

    public LinkedUniform<X, V> insert(int position, V value)
    {
        validationIndex.put(value, position);
        orderedIndex.add(position, value);
        select(value);
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
        if (validationIndex.containsKey(value)) return remove(validationIndex.get(value));

        orderedIndex.remove(value);
        modified = true;
        return this;
    }

    public LinkedUniform<X, V> refresh()
    {
        if (!modified) return this;

        getChildren().clear();
        validationIndex.clear();
        for (int ix = 0; ix < orderedIndex.size(); ix ++)
        {
            V object = orderedIndex.get(ix);

            add(object.display(), alignment.column(ix), alignment.row(ix), Priority.ALWAYS, Priority.NEVER);
            validationIndex.put(object, ix);
        }

        if (selectedObjectProperty.get() == null && 0 < orderedIndex.size()) select(orderedIndex.get(0));

        modified = false;

        return this;
    }

    public Iterator<V> iterator()
    {
        return orderedIndex.iterator();
    }

    public int size()
    {
        return orderedIndex.size();
    }

    public void removeSelected()
    {
        if (selectedObjectProperty.get() != null)
        {
            selectedObjectProperty.get().setSelected(false);
            remove(selectedObjectProperty.get());
            selectedObjectProperty.set(null);
        }
    }

    public void select(V object)
    {
        if (selectedObjectProperty.get() != null) selectedObjectProperty.get().setSelected(false);

        if (object != null) object.setSelected(true);

        selectedObjectProperty.set(object);
    }

    public void addSelectListener(Consumer<V> receptor)
    {
        selectedObjectProperty.addListener((v, r, selected) -> receptor.accept(selected));
    }
}
