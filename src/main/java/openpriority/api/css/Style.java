package openpriority.api.css;

import javafx.scene.layout.Region;

public enum Style implements IStyle
{
    DEBUG("debug"), SECTION_BUTTON("section-button"), SECTION_BUTTON_SELECTED("section-button-selected"),
    BG0("background-0"), BG1("background-1"), TEXT0("gainsboro");

    private final String css;

    Style(String css)
    {
        this.css = css;
    }

    public String css()
    {
        return css;
    }

    @Override
    public String toString()
    {
        return css;
    }
}
