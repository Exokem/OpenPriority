package openpriority.api.css;

import javafx.scene.layout.Region;
import javafx.scene.shape.Shape;

public interface IStyle
{
    String css(Part part);

    static <V extends Region> V apply(V region, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            region.getStyleClass().add(style.css(null));
        }

        return region;
    }

    static <V extends Region> V apply(V region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().add(style);
        }

        return region;
    }

    static <V extends Shape> V apply(V shape, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            shape.getStyleClass().add(style.css(null));
        }

        return shape;
    }

    static <V extends Region> V applyVarious(V region, IStyle style, Part... parts)
    {
        for (Part part : parts)
        {
            region.getStyleClass().add(style.css(part));
        }

        return region;
    }

    static <V extends Region> V remove(V region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().remove(style);
        }

        return region;
    }

    static <V extends Region> V remove(V region, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            region.getStyleClass().remove(style.css(Part.NONE));
        }

        return region;
    }

    static String toKey(String name)
    {
        return name.toLowerCase().replaceAll("_", "-");
    }

    static IStyle join(IStyle a, IStyle b)
    {
        return part -> String.format("%s-%s", a.css(part), b.css(part));
    }

    static IStyle custom(String classes)
    {
        return part -> classes;
    }

    enum Part implements IStyle
    {
        BACKGROUND, FOREGROUND, TEXT, BORDER, NONE;

        @Override
        public String css(Part part)
        {
            return toKey(name());
        }
    }
}
