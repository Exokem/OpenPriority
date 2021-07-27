package openpriority.api.component.control;

import javafx.scene.control.TextField;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public class UniformTextField extends TextField
{
    public static UniformTextField localised(String key, IStyle... styles)
    {
        return new UniformTextField(Locale.get(key), styles);
    }

    public static UniformTextField unlocalised(String key, IStyle... styles)
    {
        return new UniformTextField(key, styles);
    }

    public static UniformTextField localisedNumeric(String key, int value, IStyle... styles)
    {
        return new UniformTextField(String.format("%s %d", Locale.get(key), value), styles);
    }

    private UniformTextField(String defaultText, IStyle... styles)
    {
        super(defaultText);

        IStyle.apply(this, styles);
    }

    @Deprecated
    public UniformTextField(String key, int value, IStyle... styles)
    {
        setText(String.format("%s %d", Locale.get(key), value));

//        Locale.bind(() -> this.setText(String.format("%s %d", Locale.get(key), value)));
        IStyle.apply(this, styles);
    }
}
