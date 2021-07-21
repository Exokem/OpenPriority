package openpriority.panels;

import openpriority.OpenPriority;
import openpriority.api.responsive.DynamicRectangle;

public final class UniformMargins
{
    public static final double
        DEFAULT_MARGIN_SIDE_FACTOR = 0.20D,
        DEFAULT_MARGIN_TOP_FACTOR = 0.05D,
        DEFAULT_SPACER_VERTICAL_FACTOR = 0.02D,
        DEFAULT_HORIZONTAL_INSET = 0.01D
    ;

    private static final DynamicRectangle DEFAULT_MARGIN_SIDE = DynamicRectangle.widthOnly(OpenPriority::width, DEFAULT_MARGIN_SIDE_FACTOR);
    private static final DynamicRectangle DEFAULT_MARGIN_TOP = DynamicRectangle.heightOnly(OpenPriority::height, DEFAULT_MARGIN_TOP_FACTOR);
    private static final DynamicRectangle DEFAULT_SPACER_VERTICAL = DynamicRectangle.heightOnly(OpenPriority::height, DEFAULT_SPACER_VERTICAL_FACTOR);

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
