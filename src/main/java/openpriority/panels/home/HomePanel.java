package openpriority.panels.home;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.components.controls.*;
import openpriority.api.components.layouts.Alignment;
import openpriority.api.components.layouts.LinkedUniform;
import openpriority.api.components.layouts.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.factories.BaseUniformBuilder;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.UniformBuilder;
import openpriority.api.responsive.DynamicRegion;
import openpriority.api.responsive.IDynamicRegion;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;
import openpriority.internal.TaskController;
import openpriority.panels.UniformMargins;
import openpriority.tasks.SimpleTask;

import static openpriority.api.factories.ControlFactory.SECTION_TITLE_FACTORY;

public final class HomePanel
{
    public static final Uniform PANEL = Components.panel();

    private static final class Data
    {
        private static final LinkedUniform<Uniform, SimpleTask> TASK_VIEW = UniformBuilder.TASK_LIST_BUILDER
            .withPadding(5)
            .withGap(5)
            .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
            .limitWidth(OpenPriority::width, UniformMargins.DEFAULT_MARGIN_SIDE_FACTOR)
            .asLinkedUniform();
    }

    public static void showTask(SimpleTask task)
    {
        Data.TASK_VIEW.insert(task);
        Data.TASK_VIEW.refresh();
    }

    public static void hideTask(SimpleTask task)
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

        private static Uniform taskAssign()
        {
            final String defaultNewTask = "default-new-task", defaultNewTaskDesc = "default-new-task-desc";

            UniformTextField taskName = new UniformTextField(defaultNewTask, TaskController.tasks.size());
            TextArea taskDesc = new UniformTextArea(defaultNewTaskDesc).wrapText().preferRows(3);

            Uniform columnLeft = BaseUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)

                .add(taskName).add(taskDesc)
                .build();

            Uniform columnRight = BaseUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            UniformScrollPane componentScroller = new UniformScrollPane(columnRight);

            Uniform evenBiColumns = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .add(columnLeft)
                .add(componentScroller)
                .distributeSpaceEvenly()
                .build();

            Uniform categoryLabel = ControlFactory.SELECTOR_LABEL_FACTORY.produce("label-category-select")
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_HEIGHT, Double.MAX_VALUE)
                .alignV(VPos.CENTER);

            UniformChoiceBox categorySelect = new UniformChoiceBox(Size.REGULAR)
                .initialise(TaskController.TASK_CATEGORIES.keySet(), null)
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE);

            Uniform categorySelection = BaseUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(20)
                .add(categoryLabel, Priority.NEVER)
                .add(categorySelect, Priority.ALWAYS)
                .build();

            UniformButton assign = new UniformButton("action-assign-task")
                .invokeSizeFunction(IDynamicRegion.SizeFunction.SET_MAX_WIDTH, Double.MAX_VALUE)
                .alignH(HPos.RIGHT)
                .withAction(() ->
                {
                    SimpleTask task = SimpleTask.assign(taskName.getText())
                        .withDescription(taskDesc.getText());

                    showTask(task);

//                    Data.TASK_VIEW.add(task.sideDisplay(), Priority.ALWAYS);
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
                .add(categoryAssignHolder)
                .build();

            return taskAssign;
        }

        private static Uniform taskView()
        {
            SectionButton topLabel = SectionButton.hoverable("action-hide-taskview", IStyle.custom("border-bottom"));
            topLabel.setMaxWidth(Double.MAX_VALUE);

            Uniform taskViewPanel = BaseUniformBuilder.start(Alignment.VERTICAL)
                .add(topLabel, Priority.ALWAYS)
                .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
                .requireWidth(OpenPriority::width, Scale.MINOR.factor());

            taskViewPanel.setPrefHeight(Double.MAX_VALUE);

            Scale.scaleMaxHeight(taskViewPanel, OpenPriority::height, 1.0D);

            return taskViewPanel;
        }
    }
}
