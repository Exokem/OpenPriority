package openpriority.panel.home;

import javafx.geometry.HPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.component.control.*;
import openpriority.api.component.control.button.IconButton;
import openpriority.api.component.control.button.UniformButton;
import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.LinkedUniform;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.css.Style;
import openpriority.api.factory.BaseUniformBuilder;
import openpriority.api.factory.ControlFactory;
import openpriority.api.factory.UniformBuilder;
import openpriority.api.importer.base.ImageResource;
import openpriority.api.responsive.DynamicRegion;
import openpriority.api.responsive.DynamicResizable;
import openpriority.api.responsive.IDynamicRegion;
import openpriority.api.responsive.Locale;
import openpriority.internal.TaskController;
import openpriority.panel.UniformMargins;
import openpriority.task.Task;
import openpriority.task.TaskTemplate;

import static openpriority.api.factory.ControlFactory.SECTION_TITLE_FACTORY;

public final class HomePanel
{
    public static final Uniform PANEL = Components.panel();

    private static final class Data
    {
        private static final LinkedUniform<Uniform, Task> TASK_VIEW = UniformBuilder.TASK_LIST_BUILDER
            .withPadding(5)
            .withGap(5)
            .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
            .limitWidth(OpenPriority::width, UniformMargins.DEFAULT_MARGIN_SIDE_FACTOR)
            .asLinkedUniform();

        private static final LinkedUniform<Uniform, TaskTemplate> COMPONENT_TEMPLATES = UniformBuilder.TASK_LIST_BUILDER
            .withGap(5)
            .withPadding(5)
            .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
            .asLinkedUniform();
    }

    public static void showTask(Task task)
    {
        Data.TASK_VIEW.insert(task);
        Data.TASK_VIEW.refresh();
    }

    public static void hideTask(Task task)
    {
        Data.TASK_VIEW.remove(task);
        Data.TASK_VIEW.refresh();
    }

    private static final class Components
    {
        private static Uniform panel()
        {
            DynamicRegion margin = UniformMargins.defaultMarginSide(Color.UI_0.join(IStyle.Part.BACKGROUND));

            UniformScrollPane taskScroll = new UniformScrollPane(Data.TASK_VIEW)
                .requireWidth(OpenPriority::width, UniformMargins.DEFAULT_MARGIN_SIDE_FACTOR)
                .withScrollBarPolicies(ScrollPane.ScrollBarPolicy.NEVER, ScrollPane.ScrollBarPolicy.AS_NEEDED);



            Uniform panel = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.NEVER, Priority.ALWAYS)
                .add(taskScroll)
                .add(homeContent(), Priority.ALWAYS)
                .add(margin.copy())
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            return panel;
        }

        private static Uniform homeContent()
        {
            Uniform homeContent = BaseUniformBuilder.start(Alignment.VERTICAL)
                .withSpacers(UniformMargins::defaultSpacerVertical)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(20)
                .add(SECTION_TITLE_FACTORY.produce("section-home"), Priority.NEVER)
                .add(overview())
                .add(categoryAssign())
                .add(taskAssign())
                .build();

            return homeContent;
        }

        private static Uniform overview()
        {
            Uniform overview = UniformBuilder.MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-overview"))
                .build();

            return overview;
        }

        private static Uniform categoryAssign()
        {
            Uniform categoryAssign = UniformBuilder.MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-categories"))
                .build();

            return categoryAssign;
        }

        private static final String defaultNewTask = "default-new-task", defaultNewTaskDesc = "default-new-task-desc";

        private static Uniform taskAssign()
        {
            Uniform columnLeft = taskInformation();
            Uniform columnRight = componentScroll();

            DynamicResizable.addDelayedListener(() -> columnRight.requireHeight(columnLeft::getHeight, 1.0D), () -> columnLeft.getHeight() != 0);

            Uniform evenBiColumns = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .add(columnLeft)
                .add(columnRight)
                .distributeSpaceEvenly()
                .build();

            Uniform taskAssign = UniformBuilder.MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-tasks"))
                .add(evenBiColumns)
                .build();

            return taskAssign;
        }

        private static Uniform taskInformation()
        {
            HoverLabel taskInformationLabel = ControlFactory.SLC_INV.produce("label-new-task-details");

            UniformTextField taskName = UniformTextField.localised("default-new-task", Color.UI_0.join(IStyle.Part.BACKGROUND));
            UniformTextArea taskDesc = UniformTextArea.localised("default-new-task-desc", Color.UI_0.join(IStyle.Part.BACKGROUND)).wrapText().preferRows(5);

            Uniform titleBar = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(5)
                .add(taskInformationLabel, Priority.ALWAYS)
                .build();

            UniformChoiceBox categorySelect = new UniformChoiceBox(Size.REGULAR, Color.UI_0.join(IStyle.Part.BACKGROUND), Style.HOVERABLE)
                .initialise(TaskController.TASK_CATEGORIES.keySet(), null)
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE);

            UniformButton assign = new UniformButton("action-assign-task", IStyle.join(Color.ACCENT_1, IStyle.Part.TEXT))
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE)
                .alignH(HPos.RIGHT)
                .withAction(() ->
                {
                    Task task = Task.assign(taskName.getText())
                        .withDescription(taskDesc.getText())
                        .withComponents(Data.COMPONENT_TEMPLATES.export());

                    showTask(task);

                    String category = categorySelect.getValue();

                    taskName.setText(String.format("%s %d", Locale.get(defaultNewTask), TaskController.ASSIGNED_TASKS.size()));
                    taskDesc.setText(Locale.get(defaultNewTaskDesc));

                    if (category != null && TaskController.TASK_CATEGORIES.containsKey(category))
                    {
                        // Add the new task to the selected category
                    }

                    else
                    {

                    }
                });

            Uniform leftColumn = BaseUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(5)
                .withGap(5)
                .add(titleBar)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            Uniform leftColumnWrapper = BaseUniformBuilder.start(Alignment.VERTICAL)
                .withGap(5).defaultPriorities(Priority.ALWAYS)
                .add(leftColumn)
                .add(BaseUniformBuilder.start(Alignment.VERTICAL)
                    .defaultPriorities(Priority.ALWAYS)
                    .withPadding(5)
                    .withGap(5)
                    .add(ControlFactory.SLC_INV.produce("label-title"))
                    .add(taskName)
                    .build(Color.UI_1.join(IStyle.Part.BACKGROUND))
                )
                .add(BaseUniformBuilder.start(Alignment.VERTICAL)
                    .defaultPriorities(Priority.ALWAYS)
                    .withPadding(5)
                    .withGap(5)
                    .add(ControlFactory.SLC_INV.produce("label-description"))
                    .add(taskDesc)
                    .build(Color.UI_1.join(IStyle.Part.BACKGROUND))
                )
                .add(BaseUniformBuilder.start(Alignment.VERTICAL)
                    .defaultPriorities(Priority.ALWAYS)
                    .withPadding(5)
                    .withGap(5)
                    .add(ControlFactory.SLC_INV.produce("label-category-select"), Priority.ALWAYS)
                    .add(categorySelect, Priority.ALWAYS)
                    .build(Color.UI_1.join(IStyle.Part.BACKGROUND))
                )
                .add(assign)
                .build();

            return leftColumnWrapper;
        }

        private static Uniform componentScroll()
        {
//            LinkedUniform<Uniform, TaskTemplate> componentList = UniformBuilder.TASK_LIST_BUILDER
//                .withGap(5)
//                .withPadding(5)
//                .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
//                .asLinkedUniform();

            HoverLabel componentViewLabel = ControlFactory.SLC_INV.produce("label-component-view");
            IconButton subtract = new IconButton(ImageResource.SUBTRACT).autoResize(componentViewLabel::getHeight);

            subtract.bindSelectionFunction(selected ->
            {
                Data.COMPONENT_TEMPLATES.removeSelected();
                Data.COMPONENT_TEMPLATES.refresh();
            });

            subtract.getFunctionDisabledProperty().set(true);
            Data.COMPONENT_TEMPLATES.addSelectListener(selection -> subtract.getFunctionDisabledProperty().set(selection == null));

            IconButton add = new IconButton(ImageResource.ADD).autoResize(componentViewLabel::getHeight)
                .bindSelectionFunction(selected ->
                {
                    Data.COMPONENT_TEMPLATES.insert(0, TaskTemplate.template(Data.COMPONENT_TEMPLATES));
                    Data.COMPONENT_TEMPLATES.refresh();
                });

            Uniform componentOptions = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(5)
                .add(componentViewLabel, Priority.ALWAYS)
                .add(subtract)
                .add(add)
                .build();

            UniformScrollPane componentScroll = new UniformScrollPane(Data.COMPONENT_TEMPLATES)
                .withScrollBarPolicies(ScrollPane.ScrollBarPolicy.NEVER, ScrollPane.ScrollBarPolicy.AS_NEEDED);

            Uniform columnRight = BaseUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(5)
                .withGap(5)
                .add(componentOptions)
                .add(componentScroll, Priority.ALWAYS, Priority.ALWAYS)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            return columnRight;
        }
    }
}
