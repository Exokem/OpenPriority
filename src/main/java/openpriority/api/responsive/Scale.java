package openpriority.api.responsive;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import openpriority.OpenPriority;

import java.util.function.Supplier;

public enum Scale
{
    MINOR(0.2D), MAJOR(0.8D);

    private final double factor;

    Scale(double factor)
    {
        this.factor = factor;
    }

    public double factor()
    {
        return factor;
    }

    public double half()
    {
        return adjust(0.5D);
    }

    public double quarter()
    {
        return adjust(0.25D);
    }

    public double adjust(double factor)
    {
        return this.factor * factor;
    }

    public static double scaleWidth(Scale scale)
    {
        return OpenPriority.width() * scale.factor;
    }

    public static double scaleWidth(double factor)
    {
        return OpenPriority.width() * factor;
    }

    public static <V extends Region> V scalePrefWidth(V region, Supplier<Double> widthBasis, double factor)
    {
        DynamicResizable.addListener(() -> region.setPrefWidth(widthBasis.get() * factor));

        return region;
    }

    public static <V extends Region> V scaleMaxWidth(V region, Supplier<Double> widthBasis, double factor)
    {
        Platform.runLater(() -> region.setPrefWidth(widthBasis.get() * factor));

        DynamicResizable.addListener(() -> region.setMaxWidth(widthBasis.get() * factor));

        return region;
    }

    public static <V extends Region> V scaleMinWidth(V region, Supplier<Double> widthBasis, double factor)
    {
        Platform.runLater(() -> region.setMinWidth(widthBasis.get() * factor));

        DynamicResizable.addListener(() -> region.setMinWidth(widthBasis.get() * factor));

        return region;
    }

    public static <V extends Region> V scaleMaxHeight(V region, Supplier<Double> heightBasis, double factor)
    {
        Platform.runLater(() -> region.setPrefWidth(heightBasis.get() * factor));

        DynamicResizable.addListener(() -> region.setMaxHeight(heightBasis.get() * factor));

        return region;
    }

    public static <V extends Region> V scaleMinHeight(V region, Supplier<Double> heightBasis, double factor)
    {
        Platform.runLater(() -> region.setMinHeight(heightBasis.get() * factor));

        DynamicResizable.addListener(() -> region.setMinHeight(heightBasis.get() * factor));

        return region;
    }

    public static <V extends Region> V preferSize(V region, double width, double height)
    {
        region.setPrefSize(width, height);
        return region;
    }
}
