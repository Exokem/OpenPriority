package openpriority.api.components.controls;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import openpriority.api.components.Uniform;
import openpriority.api.css.IStyle;
import openpriority.api.css.Style;
import openpriority.api.responsive.Locale;

public class HoverLabel extends Uniform
{
    private Label label, hoveredLabel;

    private final FadeTransition fade;

    public static final double FADE_DURATION = 300;

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

        IStyle.apply(label, styles);
        IStyle.apply(hoveredLabel, styles);

        hoveredLabel.setOpacity(0.0D);

        hoverProperty().addListener((v, wsHov, isHov) ->
        {
            fade.setRate(isHov ? 1.0D : -1.0D);
            fade.play();
        });

        setPickOnBounds(false);


        add(label, 0, 0);
        add(hoveredLabel, 0, 0);
    }

    public static HoverLabel configure(String text, String color, String hoveredColor, IStyle... styles)
    {
        HoverLabel label = new HoverLabel(text, styles);

        label.label.setTextFill(Paint.valueOf(color));
        label.hoveredLabel.setTextFill(Paint.valueOf(hoveredColor));

        return label;
    }

    @Deprecated
    public HoverLabel resizeText(double size)
    {
        String style = String.format(".size-%f { -fx-font-size: %f; }", size, size);

        IStyle.apply(label, style);
        IStyle.apply(hoveredLabel, style);

//        label.setStyle(String.format("-fx-font-size: %f", size));
//        hoveredLabel.setStyle(String.format("-fx-font-size: %f", size));
        return this;
    }

    public HoverLabel setText(String text)
    {
        label.setText(text);
        hoveredLabel.setText(text);
        return this;
    }

    public void setTextFill(Paint paint)
    {
        label.setTextFill(paint);
    }

    public void setHoveredTextFill(Paint paint)
    {
        hoveredLabel.setTextFill(paint);
    }
}
