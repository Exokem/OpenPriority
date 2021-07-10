package openpriority.api.responsive;

import openpriority.OpenPriority;

public enum Scale
{
    MINOR(0.195D), MAJOR(0.805);

    final double factor;

    Scale(double factor)
    {
        this.factor = factor;
    }

    private double factor()
    {
        return factor;
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
}
