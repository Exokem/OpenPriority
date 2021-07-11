package openpriority.api.css;

import javafx.scene.layout.Region;

public interface IStyle
{
    String css(Part part);

    static void apply(Region region, IStyle... styles)
    {
        for (IStyle style : styles)
        {
            region.getStyleClass().add(style.css(null));
        }
    }

    static void apply(Region region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().add(style);
        }
    }

    static void applyVarious(Region region, IStyle style, Part... parts)
    {
        for (Part part : parts)
        {
            region.getStyleClass().add(style.css(part));
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
            region.getStyleClass().remove(style.css(Part.NONE));
        }
    }

    static String toKey(String name)
    {
        return name.toLowerCase().replaceAll("_", "-");
    }

    static IStyle join(IStyle a, IStyle b)
    {
        return part -> String.format("%s-%s", a.css(part), b.css(part));
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
