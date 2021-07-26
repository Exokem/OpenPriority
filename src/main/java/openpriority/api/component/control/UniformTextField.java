package openpriority.api.component.control;

import javafx.scene.control.TextField;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public class UniformTextField extends TextField
{
    public UniformTextField(String key, IStyle... styles)
    {
        setText(Locale.get(key));
//        Locale.bind(key, this::setText);
        IStyle.apply(this, styles);
    }

    public UniformTextField(String key, int value, IStyle... styles)
    {
        setText(String.format("%s %d", Locale.get(key), value));

//        Locale.bind(() -> this.setText(String.format("%s %d", Locale.get(key), value)));
        IStyle.apply(this, styles);
    }
}
