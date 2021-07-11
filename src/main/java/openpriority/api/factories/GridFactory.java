package openpriority.api.factories;

import javafx.application.Platform;
import javafx.scene.shape.Rectangle;
import openpriority.api.components.Uniform;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.DynamicResizable;

import java.util.function.Supplier;

public final class GridFactory
{
    public static Uniform uniform(double gap, int columns, int rows, IStyle... styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        IStyle.apply(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, IStyle... styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }

    public static Uniform uniform(double gap, int columns, int rows, String styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        IStyle.apply(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, String styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }

    public static Rectangle paddingRect(Supplier<Double> widthBasis, double widthFactor, Supplier<Double> heightBasis, double heightFactor)
    {
        Rectangle rect = new Rectangle(widthBasis.get(), heightBasis.get());
        rect.setVisible(false);

        DynamicResizable.addListener(() ->
        {
            rect.setWidth(widthBasis.get() * widthFactor);
            rect.setHeight(heightBasis.get() * heightFactor);
        });

        return rect;
    }
}
