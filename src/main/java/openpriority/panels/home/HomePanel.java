package openpriority.panels.home;

import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.css.*;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicInsets;
import openpriority.api.responsive.Scale;
import openpriority.panels.UniformMargins;

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
            Uniform panel = GridFactory.uniform(0, 1, 1, Color.UI_1.join(IStyle.Part.BACKGROUND))
                .add(taskView(), 0, 0, Priority.NEVER, Priority.ALWAYS)
                .add(homeContent(), 1, 0, Priority.SOMETIMES, Priority.ALWAYS)
            ;

            return panel;
        }

        private static Uniform homeContent()
        {
            HoverLabel homeLabel = HoverLabel.configure("section-home", Color.TEXT_0, Color.ACCENT_1, Weight.BOLD, Size.LARGE_1);

            Uniform homeContent = GridFactory.uniform(0, 0, 0, IStyle.custom("border-left"))
                .add(UniformMargins.defaultMarginTop(), 0, 0, Priority.ALWAYS)

                .add(homeLabel, 0, 1, Priority.ALWAYS, Priority.ALWAYS)
                ;

            homeLabel.setMaxHeight(Double.MAX_VALUE);
            homeContent.setMaxHeight(Double.MAX_VALUE);

            DynamicInsets.horizontalUniform(homeContent, OpenPriority::width, DEFAULT_HORIZONTAL_INSET);

            return homeContent;
        }

        private static Uniform taskView()
        {
            SectionButton taskView = SectionButton.unhoverable("label-action-view")
                .limitWidth(Double.MAX_VALUE);
            taskView.setMaxHeight(Double.MAX_VALUE);

            taskView.setVisible(false);

            Uniform taskViewPanel = GridFactory.uniform(0, 1, 4, Color.UI_0.join(IStyle.Part.BACKGROUND), Style.LEFT_MARGIN)
                .add(taskView, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
            taskViewPanel.setPrefHeight(Double.MAX_VALUE);

            Scale.scaleMaxWidth(taskViewPanel, OpenPriority::width, Scale.MINOR.factor());
            Scale.scaleMaxHeight(taskViewPanel, OpenPriority::height, 1.0D);

            return taskViewPanel;
        }
    }
}
