package openpriority.tasks;

import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factories.BaseUniformBuilder;
import openpriority.api.factories.ControlFactory;
import openpriority.internal.TaskController;
import openpriority.panels.home.HomePanel;

import java.util.ArrayList;
import java.util.List;

public class SimpleTask
{
    public static SimpleTask assign(String display)
    {
        return new SimpleTask(display);
    }

    private static int nextIdentifier = 0; // Identifier assigned to next created task (also, the number of created tasks)
    private static final List<Integer> reusableIdentifiers = new ArrayList<>();

    private String display; // Display name of the task
    private String description; // Description of the task
    private Uniform componentDisplay = null;

    private boolean completed = false;

    private final int identifier; // Immutable identifier assigned on creation
    private final long origin;

    private SimpleTask(String display)
    {
        this.display = display;
        this.identifier = nextIdentifier ++;
        this.origin = System.currentTimeMillis();

        TaskController.ASSIGNED_TASKS.put(this.toString(), this);
    }

    public SimpleTask withDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Uniform sideDisplay()
    {
        if (componentDisplay != null) return componentDisplay;

        Button hide = ControlFactory.button("remove", Double.MAX_VALUE, () ->
        {
            HomePanel.hideTask(this);
        });

        Uniform top = BaseUniformBuilder.start(Alignment.HORIZONTAL)
            .withGap(5)
            .add(ControlFactory.checkBox(display), Priority.ALWAYS)
            .add(hide, Priority.SOMETIMES)
            .build();

        componentDisplay = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withPadding(5)
            .add(top, Priority.ALWAYS)
            .build(IStyle.join(Color.UI_1, IStyle.Part.BACKGROUND));

        return componentDisplay;
    }

    public String title()
    {
        return display;
    }

    @Override
    public String toString()
    {
        return String.format("%s_%d", display, identifier);
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public void delete()
    {
        TaskController.ASSIGNED_TASKS.remove(this.toString());
        reusableIdentifiers.add(identifier);
    }
}
