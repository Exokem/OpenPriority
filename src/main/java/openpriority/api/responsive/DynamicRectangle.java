package openpriority.api.responsive;

import javafx.scene.shape.Rectangle;

import java.util.function.Supplier;

public class DynamicRectangle extends Rectangle
{
    public static DynamicRectangle empty()
    {
        return new DynamicRectangle();
    }

    public static DynamicRectangle staticRect(double width, double height)
    {
        return new DynamicRectangle(width, height);
    }

    public static DynamicRectangle widthOnly(Supplier<Double> widthBasis, double widthFactor)
    {
        DynamicRectangle rect = new DynamicRectangle();

        rect.widthBasis = widthBasis;
        rect.widthFactor = widthFactor;

        DynamicResizable.addListener(() -> rect.setWidth(rect.widthBasis.get() * rect.widthFactor));

        return rect;
    }

    public static DynamicRectangle heightOnly(Supplier<Double> heightBasis, double heightFactor)
    {
        DynamicRectangle rect = new DynamicRectangle();

        rect.heightBasis = heightBasis;
        rect.heightFactor = heightFactor;

        DynamicResizable.addListener(() -> rect.setHeight(rect.heightBasis.get() * rect.heightFactor));

        return rect;
    }

    private DynamicRectangle() {}

    public DynamicRectangle(double width, double height)
    {
        super(width, height);
    }

    private Supplier<Double> widthBasis, heightBasis;
    private double widthFactor = 0.0D, heightFactor = 0.0D;

    public DynamicRectangle width(Supplier<Double> widthBasis, double widthFactor)
    {
        this.widthBasis = widthBasis;
        this.widthFactor = widthFactor;

        if (widthBasis != null) DynamicResizable.addListener(() -> setWidth(widthBasis.get() * widthFactor));
        return this;
    }

    public DynamicRectangle height(Supplier<Double> heightBasis, double heightFactor)
    {
        this.heightBasis = heightBasis;
        this.heightFactor = heightFactor;

        if (heightBasis != null) DynamicResizable.addListener(() -> this.setHeight(this.heightBasis.get() * this.heightFactor));
        return this;
    }

    public DynamicRectangle copy()
    {
        return new DynamicRectangle().height(this.heightBasis, this.heightFactor).width(this.widthBasis, this.widthFactor);
    }
}
