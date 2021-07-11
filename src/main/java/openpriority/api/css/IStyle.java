package openpriority.api.css;

import javafx.scene.layout.Region;

public interface IStyle
{
    String css();

    static void apply(Region region, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            region.getStyleClass().add(style.css());
        }
    }

    static void apply(Region region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().add(style);
        }
    }

    static void remove(Region region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().remove(style);
        }
    }

    static void remove(Region region, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            region.getStyleClass().remove(style.css());
        }
    }
}
