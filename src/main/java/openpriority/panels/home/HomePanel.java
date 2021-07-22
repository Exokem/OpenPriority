package openpriority.panels.home;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicRegion;
import openpriority.api.responsive.Scale;
import openpriority.internal.TaskController;
import openpriority.panels.UniformMargins;

import static openpriority.api.factories.ControlFactory.SECTION_TITLE_FACTORY;

public final class HomePanel
{
    public static final Uniform PANEL = Components.panel();

    private static final class Data
    {

    }

    private static final class Components
    {
        private static Uniform panel()
        {
            DynamicRegion margin = UniformMargins.defaultMarginSide(Color.UI_0.join(IStyle.Part.BACKGROUND));

            Uniform panel = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.NEVER, Priority.ALWAYS)
                .add(taskView())
                .add(homeContent(), Priority.SOMETIMES)
                .add(margin)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            return panel;
        }

        private static Uniform homeContent()
        {
            Uniform homeContent = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .withSpacers(UniformMargins::defaultSpacerVertical)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(20)
                .add(SECTION_TITLE_FACTORY.produce("section-home"))
                .add(overview())
                .add(categoryAssign())
                .add(taskAssign())
                .build(IStyle.custom("border-lr"));

            return homeContent;
        }

        private static Uniform overview()
        {
            Uniform overview = GridFactory.MENU_SECTION_BUILDER
                .add(ControlFactory.HEADING_FACTORY.produce("label-overview"))
                .build();

            return overview;
        }

        private static Uniform categoryAssign()
        {
            Uniform categoryAssign = GridFactory.MENU_SECTION_BUILDER
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-categories"))
                .build();

            return categoryAssign;
        }

        private static Uniform taskAssign()
        {
            TextField displayName = new TextField(String.format("%s %d", "default-task-name", TaskController.tasks.size()));
            TextArea description = new TextArea("default-new-task-desc");

            description.setWrapText(true);
            description.setPrefRowCount(3);

            Uniform columnLeft = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)

                .add(displayName).add(description)
                .build();

//            UniformScrollPane

            Uniform columnRight = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            Uniform evenBiColumns = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .add(columnLeft)
                .add(columnRight)
                .distributeSpaceEvenly()
                .build();

            Uniform taskAssign = GridFactory.MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-tasks"))
                .add(evenBiColumns)
                .build();

            return taskAssign;
        }

        private static Uniform taskView()
        {
            SectionButton topLabel = SectionButton.hoverable("action-hide-taskview", IStyle.custom("border-bottom"));
            topLabel.setMaxWidth(Double.MAX_VALUE);

            Uniform taskViewPanel = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .add(topLabel, Priority.ALWAYS)
                .build(Color.UI_0.join(IStyle.Part.BACKGROUND))
                .requireWidth(OpenPriority::width, Scale.MINOR.factor());

            taskViewPanel.setPrefHeight(Double.MAX_VALUE);

            Scale.scaleMaxHeight(taskViewPanel, OpenPriority::height, 1.0D);

            return taskViewPanel;
        }
    }
}
