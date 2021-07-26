package openpriority.api.component.control;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.IDynamicRegion;

public class UniformScrollPane extends ScrollPane implements IDynamicRegion<UniformScrollPane>
{
    public UniformScrollPane(Node content, IStyle... styles)
    {
        setFitToWidth(true);
        setFitToHeight(true);
        setSnapToPixel(true);

        contentProperty().addListener(((obs, prev, next) ->
        {
            if (next instanceof Region)
            {
                Region region = (Region) next;

                setFitToHeight(region.getHeight() <= getHeight());
            }
        }));

        setContent(content);

        IStyle.apply(this, styles);
    }

    public UniformScrollPane fitContent(boolean width, boolean height)
    {
        setFitToWidth(width);
        setFitToHeight(height);

        return this;
    }

    public UniformScrollPane withScrollBarPolicies(ScrollBarPolicy horizontal, ScrollBarPolicy vertical)
    {
        setHbarPolicy(horizontal);
        setVbarPolicy(vertical);

        return this;
    }

    @Override
    public UniformScrollPane cast(Object object)
    {
        return object instanceof UniformScrollPane ? (UniformScrollPane) object : null;
    }
}
