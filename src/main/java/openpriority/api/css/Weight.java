package openpriority.api.css;

public enum Weight implements IStyle
{
    THIN, EXTRALIGHT, LIGHT, REGULAR, MEDIUM, SEMIBOLD, BOLD;

    Weight()
    {
        this.css = String.format("weight-%s", this.name().toLowerCase());
    }

    private final String css;

    @Override
    public String css()
    {
        return css;
    }
}
