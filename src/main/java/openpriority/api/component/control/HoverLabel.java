package openpriority.api.component.control;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.factory.TransitionFactory;
import openpriority.api.responsive.Locale;

public class HoverLabel extends Uniform
{
    private Label label, hoveredLabel;

    private HoverLabel(String text, IStyle... styles)
    {
        String translated = Locale.bind(text, this::setText);

        label = new Label(translated);
        hoveredLabel = new Label(translated);

        TransitionFactory.applyHoverFade(this, hoveredLabel, label, false);

        IStyle.apply(label, styles);
        IStyle.apply(hoveredLabel, styles);

        add(label, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
        add(hoveredLabel, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
    }

    public static HoverLabel configure(String text, Color base, Color hovered, IStyle... styles)
    {
        HoverLabel label = new HoverLabel(text, styles);

        IStyle.applyVarious(label.label, base, IStyle.Part.TEXT);
        IStyle.applyVarious(label.hoveredLabel, hovered, IStyle.Part.TEXT);

        return label;
    }

    @Deprecated
    public HoverLabel resizeText(double size)
    {
        String style = String.format(".size-%f { -fx-font-size: %f; }", size, size);
        IStyle.apply(label, style);
        IStyle.apply(hoveredLabel, style);

        return this;
    }

    public HoverLabel setText(String text)
    {
        label.setText(text);
        hoveredLabel.setText(text);
        return this;
    }

    @Override
    public HoverLabel cast(Object object)
    {
        return object instanceof HoverLabel ? (HoverLabel) object : null;
    }

    @Override
    public Uniform alignH(HPos position)
    {
        GridPane.setHalignment(label, position);
        GridPane.setHalignment(hoveredLabel, position);

        return super.alignH(position);
    }

    @Override
    public Uniform alignV(VPos position)
    {
        GridPane.setValignment(label, position);
        GridPane.setValignment(hoveredLabel, position);

        return super.alignV(position);
    }

    @Override
    public Uniform invokeSizeFunction(SizeFunction function, double value)
    {
        function.safeInvoke(label, value);
        function.safeInvoke(hoveredLabel, value);

        return super.invokeSizeFunction(function, value);
    }
}
