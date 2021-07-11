package openpriority.api.factories;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import openpriority.api.responsive.Locale;

public class ControlFactory
{
    public static CheckBox checkBox(String key)
    {
        CheckBox checkBox = new CheckBox(Locale.get(key));
        Locale.bind(key, checkBox::setText);

        return checkBox;
    }

    public static Button button(String key, double maxWidth, Runnable action)
    {
        Button button = new Button(Locale.get(key));
        Locale.bind(key, button::setText);

        button.setMaxWidth(maxWidth);
        button.setOnAction(a -> action.run());

        return button;
    }
}
