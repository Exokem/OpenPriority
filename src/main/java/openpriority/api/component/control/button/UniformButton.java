package openpriority.api.component.control.button;

import javafx.scene.control.Button;
import openpriority.api.css.IStyle;
import openpriority.api.css.Style;
import openpriority.api.responsive.IDynamicRegion;
import openpriority.api.responsive.Locale;

public class UniformButton extends Button implements IDynamicRegion<UniformButton>
{
    public UniformButton(String key, IStyle... styles)
    {
        super(Locale.get(key));
        Locale.bind(key, this::setText);

        IStyle.apply(this, styles);
        IStyle.apply(this, Style.HOVERABLE);
    }

    public UniformButton withAction(Runnable action)
    {
        setOnAction((event) -> action.run());
        return this;
    }

    @Override
    public UniformButton cast(Object object)
    {
        return object instanceof UniformButton ? (UniformButton) object : null;
    }
}
