package openpriority.api.components.controls;

import javafx.scene.control.ChoiceBox;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.IDynamicRegion;
import openpriority.api.responsive.Locale;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UniformChoiceBox extends ChoiceBox<String> implements IDynamicRegion<UniformChoiceBox>
{
    public UniformChoiceBox(IStyle... styles)
    {
        IStyle.apply(this, styles);
    }

    private boolean localised = false;
    private final Map<String, Runnable> actionBindings = new HashMap<>();

    public UniformChoiceBox bindActions(Map<String, Runnable> actionBindings)
    {
        this.actionBindings.clear();
        this.actionBindings.putAll(actionBindings);

        return this;
    }

    public UniformChoiceBox initialise(Collection<String> keySet, String selectedKey)
    {
        if (localised)
        {
            keySet.forEach(key -> this.getItems().add(Locale.get(key)));
            this.setValue(Locale.get(selectedKey));
        }
        else
        {
            this.getItems().addAll(keySet);
            this.setValue(selectedKey);
        }

        return this;
    }

    public UniformChoiceBox localise(Collection<String> keySet, Supplier<String> selectionProvider)
    {
        Locale.bind(() ->
        {
            this.getItems().clear();
            keySet.forEach(key -> this.getItems().add(Locale.get(key)));
            this.setValue(Locale.get(selectionProvider.get()));
        });

        localised = true;

        initialise(keySet, selectionProvider.get());

        return this;
    }

    @Override
    public UniformChoiceBox cast(Object object)
    {
        return object instanceof UniformChoiceBox ? (UniformChoiceBox) object : null;
    }
}
