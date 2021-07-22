package openpriority.api.components.controls;

import javafx.scene.control.TextArea;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.Locale;

public class UniformTextArea extends TextArea
{
    public UniformTextArea(IStyle... styles)
    {
        IStyle.apply(this, styles);
    }

    public UniformTextArea(String key, IStyle... styles)
    {
        super(Locale.get(key));
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
}
