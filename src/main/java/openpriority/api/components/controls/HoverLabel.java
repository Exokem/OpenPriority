package openpriority.api.components.controls;

import javafx.animation.FadeTransition;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import openpriority.api.components.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public class HoverLabel extends Uniform
{
    private Label label, hoveredLabel;

    private final FadeTransition fade;

    public static final double FADE_DURATION = 450;

    private HoverLabel(String text, IStyle... styles)
    {
        String translated = Locale.bind(text, this::setText);

        label = new Label(translated);
        hoveredLabel = new Label(translated);

        fade = new FadeTransition();
        fade.setDuration(Duration.millis(FADE_DURATION));
        fade.setToValue(1.0D);
        fade.setFromValue(0.0D);
        fade.setNode(hoveredLabel);

        FadeTransition altFade = new FadeTransition();
        altFade.setDuration(Duration.millis(FADE_DURATION));
        altFade.setToValue(0.0D);
        altFade.setFromValue(1.0D);
        altFade.setNode(label);


        IStyle.apply(label, styles);
        IStyle.apply(hoveredLabel, styles);

        hoveredLabel.setOpacity(0.0D);

        hoverProperty().addListener((v, wsHov, isHov) ->
        {
            fade.setRate(isHov ? 1.0D : -1.0D);
            altFade.setRate(isHov ? 1.0D : -1.0D);

            fade.play();
            altFade.play();
        });

        setPickOnBounds(false);

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
