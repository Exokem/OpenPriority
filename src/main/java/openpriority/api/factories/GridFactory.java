package openpriority.api.factories;

import openpriority.api.components.Uniform;
import openpriority.api.css.Style;
import openpriority.api.css.StyleApplicator;

public final class GridFactory
{
    public static Uniform uniform(double gap, int columns, int rows, Style... styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        StyleApplicator.addStyle(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, Style... styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }

    public static Uniform uniform(double gap, int columns, int rows, String styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        Style.apply(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, String styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }
}
