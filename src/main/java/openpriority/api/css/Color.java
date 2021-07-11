package openpriority.api.css;

public enum Color implements IStyle
{
    ACCENT_1, UI_0, UI_1, UI_2, UI_3, TEXT_0;

    @Override
    public String css(Part part)
    {
        if (part == null) return IStyle.toKey(name());

        return String.format("%s-%s", IStyle.toKey(name()), IStyle.toKey(part.name()));
    }
}
