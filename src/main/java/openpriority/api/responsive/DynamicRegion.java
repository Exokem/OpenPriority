package openpriority.api.responsive;

import javafx.scene.layout.Region;

import java.util.function.Supplier;

public class DynamicRegion extends Region
{
    public static DynamicRegion empty()
    {
        return new DynamicRegion();
    }

    public static DynamicRegion widthOnly(Supplier<Double> widthBasis, double widthFactor)
    {
        DynamicRegion rect = new DynamicRegion();

        rect.widthBasis = widthBasis;
        rect.widthFactor = widthFactor;

        DynamicResizable.addListener(() -> rect.setMinWidth(rect.widthBasis.get() * rect.widthFactor));
        DynamicResizable.addListener(() -> rect.setMaxHeight(Double.MAX_VALUE));

        return rect;
    }

    public static DynamicRegion heightOnly(Supplier<Double> heightBasis, double heightFactor)
    {
        DynamicRegion rect = new DynamicRegion();

        rect.heightBasis = heightBasis;
        rect.heightFactor = heightFactor;

        DynamicResizable.addListener(() -> rect.setMinHeight(rect.heightBasis.get() * rect.heightFactor));

        return rect;
    }

    private DynamicRegion() {}

    private Supplier<Double> widthBasis, heightBasis;
    private double widthFactor = 0.0D, heightFactor = 0.0D;

    public DynamicRegion width(Supplier<Double> widthBasis, double widthFactor)
    {
        this.widthBasis = widthBasis;
        this.widthFactor = widthFactor;

        if (widthBasis != null) DynamicResizable.addListener(() -> setMinWidth(widthBasis.get() * widthFactor));
        return this;
    }

    public DynamicRegion height(Supplier<Double> heightBasis, double heightFactor)
    {
        this.heightBasis = heightBasis;
        this.heightFactor = heightFactor;

        if (heightBasis != null) DynamicResizable.addListener(() -> this.setMinHeight(this.heightBasis.get() * this.heightFactor));
        return this;
    }

    public DynamicRegion copy()
    {
        return new DynamicRegion().height(this.heightBasis, this.heightFactor).width(this.widthBasis, this.widthFactor);
    }

    public DynamicRegion show()
    {
        setVisible(true);

        return this;
    }
}
