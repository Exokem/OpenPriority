package openpriority.api.css;

import javafx.scene.layout.Region;

public enum Style
{
    DEBUG("debug"), SECTION_BUTTON("section-button"), SECTION_BUTTON_SELECTED("section-button-selected"),
    BG0("background-0"), BG1("background-1");

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

    public static void remove(Region region, String styles)
    {
        for (String style : styles.split(" "))
        {
            region.getStyleClass().remove(style);
        }
    }

    public static void remove(Region region, Style... styles)
    {
        for (Style style : styles)
        {
            region.getStyleClass().remove(style.css);
        }
    }

    @Override
    public String toString()
    {
        return css;
    }
}
