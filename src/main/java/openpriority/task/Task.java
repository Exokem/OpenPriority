package openpriority.task;

import javafx.geometry.Insets;
import javafx.scene.layout.Priority;
import openpriority.api.component.control.HoverLabel;
import openpriority.api.component.control.UniformTextArea;
import openpriority.api.component.control.button.IconButton;
import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.ILinkedUniformDisplayable;
import openpriority.api.component.layout.LinkedUniform;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factory.BaseUniformBuilder;
import openpriority.api.factory.ControlFactory;
import openpriority.api.factory.UniformBuilder;
import openpriority.api.importer.base.ImageResource;
import openpriority.internal.TaskController;
import openpriority.panel.home.HomePanel;

import java.util.ArrayList;
import java.util.List;

public class Task implements ILinkedUniformDisplayable<Uniform>
{
    public static Task assign(String display)
    {
        return new Task(display);
    }

    public static Task simple(String display, String description)
    {
        Task task = new Task(display);
        task.description = description;
        task.simple = true;

        return task;
    }

    private static int nextIdentifier = 0; // Identifier assigned to next created task (also, the number of created tasks)
    private static final List<Integer> reusableIdentifiers = new ArrayList<>();

    private String display; // Display name of the task
    private String description; // Description of the task
    protected Uniform totalDisplay = null;
    protected LinkedUniform<Uniform, Task> componentHolder = UniformBuilder.TASK_LIST_BUILDER
        .withPadding(5)
        .withGap(5)
        .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
//        .limitWidth(OpenPriority::width, UniformMargins.DEFAULT_MARGIN_SIDE_FACTOR)
        .asLinkedUniform();

    private boolean completed = false;
    private boolean simple = true;

    private final int identifier; // Immutable identifier assigned on creation
    private final long origin;

    protected Task()
    {
        identifier = -1;
        origin = -1;
    }

    protected Task(String display)
    {
        this.display = display;
        this.identifier = nextIdentifier ++;
        this.origin = System.currentTimeMillis();

        TaskController.ASSIGNED_TASKS.put(this.toString(), this);
    }

    public Task withDescription(String description)
    {
        this.description = description;
        return this;
    }

    public Task withComponents(List<TaskTemplate> templates)
    {
        templates.forEach(template -> componentHolder.insert(template.asTask()));
        componentHolder.refresh();

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
        if (totalDisplay != null) return totalDisplay;

        HoverLabel label = ControlFactory.SELECTOR_LABEL_FACTORY.produce(display);
        IconButton completed = new IconButton(ImageResource.SELECTION).autoResize(label::getHeight);

        UniformTextArea descriptionArea = UniformTextArea.unlocalised(description)
            .setImmutable(true)
            .preferRows(3)
            .wrapText();

        Uniform description = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withGap(5)
            .add(descriptionArea, Priority.ALWAYS)
            .add(componentHolder.size() == 0 ? null : componentHolder, Priority.ALWAYS)
            .build()
            .inset(new Insets(5, 0, 0, 0));

        IconButton hideTask = new IconButton(ImageResource.CLOSE_TASK).autoResize(label::getHeight)
            .bindSelectionFunction(selected -> HomePanel.hideTask(this));
        IconButton expandTask = new IconButton(ImageResource.EXPAND)
            .autoResize(label::getHeight)
            .bindSelectionFunction(selected ->
            {
                description.setMaxHeight(selected ? Double.MAX_VALUE : 0);
                description.setVisible(selected);
                this.delete();
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

        totalDisplay = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withPadding(5)
            .add(top, Priority.ALWAYS)
            .add(description, Priority.ALWAYS)
            .build(IStyle.join(Color.UI_1, IStyle.Part.BACKGROUND));

        return totalDisplay;
    }
}
