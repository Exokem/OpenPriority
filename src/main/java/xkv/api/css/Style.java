package xkv.api.css;

import javafx.scene.layout.Region;

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


    @Override
    public String toString()
    {
        return css;
    }
}
