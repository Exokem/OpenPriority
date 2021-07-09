package xkv.api.css;

import javafx.scene.layout.Region;

import java.util.HashSet;
import java.util.Set;

public enum Style
{
    ;

    private final String css;

    Style(String css)
    {
        this.css = css;
    }

    public String css()
    {
        return css;
    }

    public static void apply(Region region, Style... styles)
    {
        for (Style style : styles)
        {
            region.getStyleClass().add(style.css);
        }
    }

    public static void apply(Region region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().add(style);
        }
    }

    @Override
    public String toString()
    {
        return css;
    }
}
