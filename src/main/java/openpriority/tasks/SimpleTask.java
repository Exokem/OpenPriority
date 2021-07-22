package openpriority.tasks;

import javafx.scene.layout.Priority;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;

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
    }

    public SimpleTask withDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Uniform sideDisplay()
    {
        if (componentDisplay != null) return componentDisplay;

        componentDisplay = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
            .add(ControlFactory.checkBox(display), Priority.ALWAYS)
//            .add(ControlFactory.label(description), Priority.ALWAYS)
            .build();

        return componentDisplay;
    }

    public void setCompleted(boolean completed)
    {
        this.completed = completed;
    }

    public void delete()
    {
        reusableIdentifiers.add(identifier);
    }
}
