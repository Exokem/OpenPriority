package openpriority.api.css;

public enum Size implements IStyle
{
    REGULAR(15), LARGE(40), LARGE_1(50);

    Size(int size)
    {
        this.css = String.format("text-size-%s", IStyle.toKey(name()));
    }

    private final String css;

    @Override
    public String css(Part part)
    {
        return css;
    }
}
