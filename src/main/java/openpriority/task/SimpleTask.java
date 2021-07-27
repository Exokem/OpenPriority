package openpriority.task;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import openpriority.api.component.control.HoverLabel;
import openpriority.api.component.control.UniformTextArea;
import openpriority.api.component.control.button.IconButton;
import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.ILinkedUniformDisplayable;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factory.BaseUniformBuilder;
import openpriority.api.factory.ControlFactory;
import openpriority.api.importer.base.ImageResource;
import openpriority.internal.TaskController;
import openpriority.panel.home.HomePanel;

import java.util.ArrayList;
import java.util.List;

public class SimpleTask implements ILinkedUniformDisplayable<Uniform>
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

    @Override
    public Uniform display()
    {
        if (componentDisplay != null) return componentDisplay;

        HoverLabel label = ControlFactory.SELECTOR_LABEL_FACTORY.produce(display);
        IconButton completed = new IconButton().bindImage(ImageResource.SELECTION).autoResize(label::getHeight);

        UniformTextArea descriptionArea = UniformTextArea.unlocalised(description)
            .setImmutable(true)
            .preferRows(3)
            .wrapText();

        Uniform description = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withGap(5)
            .add(descriptionArea, Priority.ALWAYS)
            .build()
            .inset(new Insets(5, 0, 0, 0));

        IconButton hideTask = new IconButton().bindImage(ImageResource.CLOSE_TASK).autoResize(label::getHeight)
            .bindSelectionFunction(selected -> HomePanel.hideTask(this));
        IconButton expandTask = new IconButton()
            .bindImage(ImageResource.EXPAND)
            .autoResize(label::getHeight)
            .bindSelectionFunction(selected ->
            {
                description.setMaxHeight(selected ? Double.MAX_VALUE : 0);
                description.setVisible(selected);
            });

        Uniform top = BaseUniformBuilder.start(Alignment.HORIZONTAL)
            .withGap(5)
            .add(completed, Priority.SOMETIMES, Priority.NEVER)
            .add(label, Priority.ALWAYS, Priority.NEVER)
            .add(expandTask, Priority.SOMETIMES, Priority.NEVER)
            .add(hideTask, Priority.SOMETIMES, Priority.NEVER)
            .build();

        description.requireHeight(0);
        description.setVisible(false);

        componentDisplay = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withPadding(5)
            .add(top, Priority.ALWAYS)
            .add(description, Priority.ALWAYS)
            .build(IStyle.join(Color.UI_1, IStyle.Part.BACKGROUND));

        return componentDisplay;
    }
}
