package openpriority.panel;

import openpriority.OpenPriority;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.DynamicRegion;

public final class UniformMargins
{
    public static final double
        DEFAULT_MARGIN_SIDE_FACTOR = 0.20D,
        DEFAULT_MARGIN_TOP_FACTOR = 0.05D,
        DEFAULT_SPACER_VERTICAL_FACTOR = 0.02D,
        DEFAULT_HORIZONTAL_INSET = 0.01D
    ;

    private static final DynamicRegion DEFAULT_MARGIN_SIDE = DynamicRegion.widthOnly(OpenPriority::width, DEFAULT_MARGIN_SIDE_FACTOR);
    private static final DynamicRegion DEFAULT_MARGIN_TOP = DynamicRegion.heightOnly(OpenPriority::height, DEFAULT_MARGIN_TOP_FACTOR);
    private static final DynamicRegion DEFAULT_SPACER_VERTICAL = DynamicRegion.heightOnly(OpenPriority::height, DEFAULT_SPACER_VERTICAL_FACTOR);

    public static DynamicRegion defaultMarginSide(IStyle... styles)
    {
        return IStyle.apply(DEFAULT_MARGIN_SIDE.copy(), styles);
    }

    public static DynamicRegion defaultMarginTop(IStyle... styles)
    {
        return IStyle.apply(DEFAULT_MARGIN_TOP.copy(), styles);
    }

    public static DynamicRegion defaultSpacerVertical(IStyle... styles)
    {
        return IStyle.apply(DEFAULT_SPACER_VERTICAL.copy(), styles);
    }
}
