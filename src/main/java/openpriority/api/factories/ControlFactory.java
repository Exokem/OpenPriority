package openpriority.api.factories;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import openpriority.api.Option;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.*;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;

import java.util.function.Supplier;

public class ControlFactory
{
    public static CheckBox checkBox(String key, IStyle... styles)
    {
        CheckBox checkBox = new CheckBox(Locale.get(key));
        Locale.bind(key, checkBox::setText);

        IStyle.apply(checkBox, styles);

        return checkBox;
    }

    public static CheckBox optionCheckBox(String key, Option<Boolean> option,Runnable effect, IStyle... styles)
    {
        CheckBox checkBox = checkBox(key, styles);

        checkBox.setSelected(option.get());
        checkBox.selectedProperty().addListener((obs, prev, next) ->
        {
            option.set(next);
            if (effect != null) effect.run();
        });

        return checkBox;
    }

    public static TextArea textArea(String key, IStyle... styles)
    {
        TextArea area = new TextArea(Locale.get(key));
        return IStyle.apply(area, styles);
    }

    public static Button button(String key, double maxWidth, Runnable action, IStyle... styles)
    {
        Button button = new Button(Locale.get(key));
        Locale.bind(key, button::setText);

        button.setMaxWidth(maxWidth);
        button.setOnAction(a -> action.run());

        IStyle.apply(button, styles);
        IStyle.apply(button, Style.HOVERABLE);

        return button;
    }

    public static Button dynamicButton(String key, Supplier<Double> basis, double factor, Runnable action, IStyle... styles)
    {
        Button button = new Button(Locale.get(key));
        Locale.bind(key, button::setText);

        Scale.scaleMaxWidth(button, basis, factor);
        button.setOnAction(a -> action.run());

        IStyle.apply(button, styles);
        IStyle.apply(button, Style.HOVERABLE);

        return button;
    }

    public static Label label(String key, IStyle... styles)
    {
        Label label;

        if (key.isEmpty() || key.isBlank())
        {
            label = new Label();
        }

        else
        {
            label = new Label(Locale.get(key));
            Locale.bind(key, label::setText);
        }

        IStyle.apply(label, styles);

        return label;
    }

    public static Label sizedLabel(String key, double maxWidth, double maxHeight, IStyle... styles)
    {
        Label label = label(key, styles);

        label.setMaxWidth(maxWidth);
        label.setMaxHeight(maxHeight);

        return label;
    }

    public static final HoverLabelFactory SECTION_TITLE_FACTORY = new HoverLabelFactory(Color.TEXT_0, Color.ACCENT_1, Weight.BOLD, Size.LARGE_1);
    public static final HoverLabelFactory HEADING_FACTORY = new HoverLabelFactory(Color.TEXT_0, Color.ACCENT_1, Weight.SEMIBOLD, Size.LARGE);

    public static final class HoverLabelFactory
    {
        private final Color normal, hovered;
        private final Weight weight;
        private final Size size;

        public HoverLabelFactory(Color normal, Color hovered, Weight weight, Size size)
        {
            this.normal = normal;
            this.hovered = hovered;
            this.weight = weight;
            this.size = size;
        }

        public HoverLabel produce(String key)
        {
            return HoverLabel.configure(key, normal, hovered, weight, size);
        }
    }
}
