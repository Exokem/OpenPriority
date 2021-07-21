package openpriority.panels.home;

import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicInsets;
import openpriority.api.responsive.Scale;
import openpriority.panels.UniformMargins;

import static openpriority.api.factories.ControlFactory.SECTION_TITLE_FACTORY;
import static openpriority.panels.UniformMargins.DEFAULT_HORIZONTAL_INSET;

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
            Uniform panel = GridFactory.uniform(0, 0, 0, Color.UI_1.join(IStyle.Part.BACKGROUND))
                .add(taskView(), 0, 0, Priority.NEVER, Priority.ALWAYS)
                .add(homeContent(), 1, 0, Priority.SOMETIMES, Priority.ALWAYS)
                .add(UniformMargins.defaultMarginSide(Color.UI_0.join(IStyle.Part.BACKGROUND)), 2, 0, Priority.NEVER, Priority.ALWAYS)
            ;

            return panel;
        }

        private static Uniform homeContent()
        {
            Uniform homeContent = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .withSpacers(UniformMargins::defaultSpacerVertical, false)
                .defaultPriorities(Priority.ALWAYS)
                .add(SECTION_TITLE_FACTORY.produce("section-home"))
                .add(overview())
                .add(categoryAssign())
                .add(taskAssign())
                .build(IStyle.custom("border-lr"));

            DynamicInsets.horizontalUniform(homeContent, OpenPriority::width, DEFAULT_HORIZONTAL_INSET);

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
            Uniform taskAssign = GridFactory.MENU_SECTION_BUILDER
                .add(ControlFactory.HEADING_FACTORY.produce("label-assign-tasks"))
                .build();

            return taskAssign;
        }

        private static Uniform taskView()
        {
            SectionButton topLabel = Scale.scaleMaxWidth(SectionButton.hoverable("action-hide-taskview", IStyle.custom("border-bottom")),
                OpenPriority::width, Scale.MINOR.factor());

            Uniform taskViewPanel = GridFactory.uniform(0, 1, 1, Color.UI_0.join(IStyle.Part.BACKGROUND))
                .add(topLabel, 0, 0, Priority.ALWAYS)
                ;
            taskViewPanel.setPrefHeight(Double.MAX_VALUE);

            Scale.scaleMaxWidth(taskViewPanel, OpenPriority::width, Scale.MINOR.factor());
            Scale.scaleMaxHeight(taskViewPanel, OpenPriority::height, 1.0D);

            return taskViewPanel;
        }
    }
}
