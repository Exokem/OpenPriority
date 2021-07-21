package openpriority.api.responsive;

import javafx.geometry.Insets;
import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class DynamicInsets extends Insets
{
    public static void horizontalUniform(Region region, Supplier<Double> basis, double factor)
    {
        DynamicResizable.addListener(() -> region.setPadding(new DynamicInsets(0, basis.get() * factor, 0, basis.get() * factor)));
    }

    private DynamicInsets(double top, double right, double bottom, double left)
    {
        super(top, right, bottom, left);
    }

    private DynamicInsets(double topRightBottomLeft)
    {
        super(topRightBottomLeft);
    }
}
