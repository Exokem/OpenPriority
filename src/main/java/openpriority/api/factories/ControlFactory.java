package openpriority.api.factories;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public class ControlFactory
{
    public static CheckBox checkBox(String key, IStyle... styles)
    {
        CheckBox checkBox = new CheckBox(Locale.get(key));
        Locale.bind(key, checkBox::setText);

        IStyle.apply(checkBox, styles);

        return checkBox;
    }

    public static Button button(String key, double maxWidth, Runnable action, IStyle... styles)
    {
        Button button = new Button(Locale.get(key));
        Locale.bind(key, button::setText);

        button.setMaxWidth(maxWidth);
        button.setOnAction(a -> action.run());

        IStyle.apply(button, styles);

        return button;
    }

    public static Label label(String key, IStyle... styles)
    {
        Label label = new Label(Locale.get(key));
        Locale.bind(key, label::setText);

        IStyle.apply(label, styles);

        return label;
    }
}
