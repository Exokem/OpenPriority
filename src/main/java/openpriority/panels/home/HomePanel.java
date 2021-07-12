package openpriority.panels.home;

import javafx.scene.layout.Priority;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.css.Weight;
import openpriority.api.factories.GridFactory;
import openpriority.panels.UniformMargins;

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
            HoverLabel homeLabel = HoverLabel.configure("section-home", Color.TEXT_0, Color.ACCENT_1, Weight.BOLD, Size.LARGE_1);
            Uniform labelHolder = GridFactory.autoUniform(20, 1, 1, Color.UI_0.join(IStyle.Part.BACKGROUND))
                .add(homeLabel, 1, 1, Priority.ALWAYS);

            Uniform panel = GridFactory.autoUniform(0, 3, 2, Color.UI_1.join(IStyle.Part.BACKGROUND))
                .add(UniformMargins.defaultMarginSide(), 0, 0, Priority.NEVER)
                .add(UniformMargins.defaultMarginSide(), 2, 0, Priority.NEVER)
                .add(UniformMargins.defaultMarginTop(), 1, 0, Priority.NEVER)
                .add(labelHolder, 1, 1, Priority.ALWAYS);

            return panel;
        }
    }
}
