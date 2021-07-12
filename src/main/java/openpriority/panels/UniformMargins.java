package openpriority.panels;

import openpriority.OpenPriority;
import openpriority.api.responsive.DynamicRectangle;

public final class UniformMargins
{
    private static final DynamicRectangle DEFAULT_MARGIN_SIDE = DynamicRectangle.empty()
        .width(OpenPriority::width, 0.25D);

    private static final DynamicRectangle DEFAULT_MARGIN_TOP = DynamicRectangle.heightOnly(OpenPriority::height, 0.05D);

    private static final DynamicRectangle DEFAULT_SPACER_VERTICAL = DynamicRectangle.heightOnly(OpenPriority::height, 0.02D);

    public static DynamicRectangle defaultMarginSide()
    {
        return DEFAULT_MARGIN_SIDE.copy();
    }

    public static DynamicRectangle defaultMarginTop()
    {
        return DEFAULT_MARGIN_TOP.copy();
    }

    public static DynamicRectangle defaultSpacerVertical()
    {
        return DEFAULT_SPACER_VERTICAL.copy();
    }
}
