package openpriority.api.factories;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import openpriority.api.Option;
import openpriority.api.css.IStyle;
import openpriority.api.css.Style;
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
}
