package openpriority.task;

import openpriority.api.component.control.UniformTextArea;
import openpriority.api.component.control.UniformTextField;
import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.LinkedUniform;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Style;
import openpriority.api.factory.BaseUniformBuilder;
import openpriority.api.factory.ControlFactory;

public class TaskTemplate extends Task
{
    public static TaskTemplate template(LinkedUniform<Uniform, TaskTemplate> container)
    {
        return new TaskTemplate(container);
    }

    protected TaskTemplate(LinkedUniform<Uniform, TaskTemplate> container)
    {
        this.container = container;
    }

    private final LinkedUniform<Uniform, TaskTemplate> container;

    private UniformTextField title = UniformTextField.localised("default-new-component");
    private UniformTextArea description = UniformTextArea.localised("default-new-component-desc").preferRows(2);

    public Task asTask()
    {
        Task task = new Task(title.getText()).withDescription(description.getText());

        return task;
    }

    @Override
    public String key()
    {
        return super.key();
    }

    @Override
    public Uniform display()
    {
        if (this.componentDisplay != null) return componentDisplay;

        componentDisplay = BaseUniformBuilder.start(Alignment.VERTICAL)
            .withPadding(5)
            .withGap(5)
            .add(ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-title"))
            .add(title)
            .add(ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-description"))
            .add(description)
            .build(IStyle.join(Color.UI_1, IStyle.Part.BACKGROUND));

        componentDisplay.setOnMouseClicked(value -> container.select(this));

        return componentDisplay;
    }

    @Override
    public void setSelected(boolean selected)
    {
        if (selected) IStyle.apply(this.display(), Style.SELECTED);
        else IStyle.remove(this.display(), Style.SELECTED);
    }
}
