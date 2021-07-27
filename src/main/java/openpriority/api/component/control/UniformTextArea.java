package openpriority.api.component.control;

import javafx.scene.control.TextArea;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public final class UniformTextArea extends TextArea
{
    public static UniformTextArea localised(String key, IStyle... styles)
    {
        return new UniformTextArea(Locale.get(key), styles);
    }

    public static UniformTextArea unlocalised(String text, IStyle... styles)
    {
        return new UniformTextArea(text, styles);
    }

    private UniformTextArea(String defaultText, IStyle... styles)
    {
        super(defaultText);
        IStyle.apply(this, styles);
    }

    public UniformTextArea(IStyle... styles)
    {
        IStyle.apply(this, styles);
    }

    public UniformTextArea wrapText()
    {
        setWrapText(true);
        return this;
    }

    public UniformTextArea preferRows(int rows)
    {
        setPrefRowCount(rows);
        return this;
    }

    public UniformTextArea setImmutable(boolean immutable)
    {
        setFocusTraversable(!immutable);
        setEditable(!immutable);

        return this;
    }
}
