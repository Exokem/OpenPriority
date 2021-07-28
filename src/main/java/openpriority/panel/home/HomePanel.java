package openpriority.panel.home;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
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
            UniformTextField taskName = UniformTextField.localisedNumeric(defaultNewTask, TaskController.tasks.size());
            TextArea taskDesc = UniformTextArea.localised(defaultNewTaskDesc).wrapText().preferRows(5);

//            Uniform columnLeft = BaseUniformBuilder.start(Alignment.VERTICAL)
//                .defaultPriorities(Priority.SOMETIMES)
//                .withGap(20)
//                .add(taskName).add(taskDesc, Priority.SOMETIMES, Priority.ALWAYS)
//                .build();

//            Uniform componentScroll = componentScroll();

            Uniform columnLeft = taskInformation();
            Uniform columnRight = componentScroll();

            DynamicResizable.addDelayedListener(() -> columnRight.requireHeight(columnLeft::getHeight, 1.0D), () -> columnLeft.getHeight() != 0);

//            columnRight.requireHeight((double) Options.General.PREFERRED_HEIGHT.get() * 0.25);

            Uniform evenBiColumns = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .add(columnLeft)
                .add(columnRight)
                .distributeSpaceEvenly()
                .build();

            Uniform categoryLabel = ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-category-select")
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_HEIGHT, Double.MAX_VALUE)
                .alignV(VPos.CENTER);

            UniformChoiceBox categorySelect = new UniformChoiceBox(Size.REGULAR)
                .initialise(TaskController.TASK_CATEGORIES.keySet(), null)
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE);

            Uniform categorySelection = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(10)
                .add(categoryLabel, Priority.NEVER)
                .add(categorySelect, Priority.ALWAYS)
                .build();

            UniformButton assign = new UniformButton("action-assign-task")
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE)
                .alignH(HPos.RIGHT)
                .withAction(() ->
                {
                    Task task = Task.assign(taskName.getText())
                        .withDescription(taskDesc.getText());

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

            Uniform categoryAssignHolder = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(20)
                .add(categorySelection)
                .add(assign, Priority.ALWAYS)
                .distributeSpaceEvenly()
                .build();

            Uniform taskAssign = UniformBuilder.MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-tasks"))
                .add(evenBiColumns)
//                .add(categoryAssignHolder)
                .build();

            return taskAssign;
        }

        private static Uniform taskInformation()
        {
            HoverLabel taskInformationLabel = ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-new-task");

            UniformTextField taskName = UniformTextField.localised("default-new-task");
            UniformTextArea taskDesc = UniformTextArea.localised("default-new-task-desc").wrapText().preferRows(5);

            Uniform titleBar = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(5)
                .add(taskInformationLabel, Priority.ALWAYS)
                .build();

            UniformChoiceBox categorySelect = new UniformChoiceBox(Size.REGULAR, Color.UI_2.join(IStyle.Part.BACKGROUND), Style.HOVERABLE)
                .initialise(TaskController.TASK_CATEGORIES.keySet(), null)
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE);

            Uniform information = BaseUniformBuilder.start(Alignment.VERTICAL)
                .withPadding(5).withGap(5)
                .add(ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-title"))
                .add(taskName)
                .add(ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-description"))
                .add(taskDesc)
                .add(ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-category-select"), Priority.ALWAYS)
                .add(categorySelect, Priority.ALWAYS)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            UniformButton assign = new UniformButton("action-assign-task")
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE)
                .alignH(HPos.RIGHT)
                .withAction(() ->
                {
                    Task task = Task.assign(taskName.getText())
                        .withDescription(taskDesc.getText());

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

            Uniform informationHolder = BaseUniformBuilder.start(Alignment.VERTICAL)
                .withGap(5)
                .withPadding(5)
                .add(information)
                .build(Color.UI_0.join(IStyle.Part.BACKGROUND));

            Uniform leftColumn = BaseUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(5)
                .withGap(5)
                .add(titleBar)
                .add(informationHolder, Priority.ALWAYS)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            Uniform leftColumnWrapper = BaseUniformBuilder.start(Alignment.VERTICAL)
                .withGap(5).defaultPriorities(Priority.ALWAYS)
                .add(leftColumn).add(assign)
                .build();

            return leftColumnWrapper;
        }

        private static Uniform componentScroll()
        {
            LinkedUniform<Uniform, TaskTemplate> componentList = UniformBuilder.TASK_LIST_BUILDER
                .withGap(5)
                .withPadding(5)
                .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
                .asLinkedUniform();

            HoverLabel componentViewLabel = ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-component-view");
            IconButton subtract = new IconButton(ImageResource.SUBTRACT).autoResize(componentViewLabel::getHeight);

            subtract.bindSelectionFunction(selected ->
            {
                componentList.removeSelected();
                componentList.refresh();
            });

            subtract.getFunctionDisabledProperty().set(true);
            componentList.addSelectListener(selection -> subtract.getFunctionDisabledProperty().set(selection == null));

            IconButton add = new IconButton(ImageResource.ADD).autoResize(componentViewLabel::getHeight)
                .bindSelectionFunction(selected ->
                {
                    componentList.insert(0, TaskTemplate.template(componentList));
                    componentList.refresh();
                });

            Uniform componentOptions = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(5)
                .add(componentViewLabel, Priority.ALWAYS)
                .add(subtract)
                .add(add)
                .build();

            UniformScrollPane componentScroll = new UniformScrollPane(componentList)
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
