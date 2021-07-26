package openpriority.api.components.layouts;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import openpriority.api.responsive.IDynamicRegion;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Uniform extends GridPane implements IDynamicRegion<Uniform>
{
    public Uniform()
    {
        super();
        setSnapToPixel(true);
    }

    private int columns = 0, rows = 0;
    private int indices = 0;

    protected Alignment alignment;
    private final Map<String, ? extends Region> contentIndex = new HashMap<>();

    public final Uniform inset(double inset)
    {
        Insets insets = new Insets(inset);

        setPadding(insets);

        return this;
    }

    public final Uniform percentWidth(double percent)
    {
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(percent);
        return this.addColumnConstraints(constraints);
    }

    public final Uniform percentHeight(double percent)
    {
        RowConstraints constraints = new RowConstraints();
        constraints.setPercentHeight(percent);
        return this.addRowConstraints(constraints);
    }

    public final Uniform addColumnConstraints(ColumnConstraints... constraints)
    {
        this.getColumnConstraints().addAll(Arrays.asList(constraints));
        return this;
    }

    public final Uniform addColumnConstraints(Collection<ColumnConstraints> constraints)
    {
        this.getColumnConstraints().addAll(constraints);
        return this;
    }

    public final Uniform addRowConstraints(RowConstraints... constraints)
    {
        this.getRowConstraints().addAll(Arrays.asList(constraints));
        return this;
    }

    public final Uniform addRowConstraints(Collection<RowConstraints> constraints)
    {
        this.getRowConstraints().addAll(constraints);
        return this;
    }

    public final Uniform align(Alignment axis)
    {
        this.alignment = axis;
        return this;
    }

    public <X extends Node, V extends ILinkedUniformDisplayable<X>> LinkedUniform<X, V> asLinkedUniform()
    {
        return (LinkedUniform<X, V>) this;
    }

    public final Uniform add(Node child, Priority... expansions)
    {
        switch (alignment)
        {
            case VERTICAL -> add(child, 0, indices ++, expansions);
            case HORIZONTAL -> add(child, indices ++, 0, expansions);
        }

        return this;
    }

    public final Uniform add(Node child, int column, int row, Priority... expansions)
    {
        return add(child, column, row, 1, 1, expansions);
    }

    public Uniform add(Node child, int column, int row, int colSpan, int rowSpan, Priority... expansions)
    {
        try
        {
            GridPane.setHgrow(child, expansions[0]);
            GridPane.setVgrow(child, expansions[1]);
        }

        catch (ArrayIndexOutOfBoundsException ignored) {}

        super.add(child, column, row, colSpan, rowSpan);

        columns = Math.max(columns, column + 1);
        rows = Math.max(rows, row + 1);

        indices ++;

        return this;
    }

    @Override
    public final void add(Node child, int columnIndex, int rowIndex)
    {
        add(child, columnIndex, rowIndex, new Priority[]{});
    }

    @Override
    public final void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
    {
        add(child, columnIndex, rowIndex, colspan, rowspan, new Priority[]{});
    }

    @Deprecated @Override
    public final void addRow(int rowIndex, Node... children)
    {
        super.addRow(rowIndex, children);
    }

    @Deprecated @Override
    public final void addColumn(int columnIndex, Node... children)
    {
        super.addColumn(columnIndex, children);
    }

    public int columns()
    {
        return columns;
    }

    public int rows()
    {
        return rows;
    }

    public Map<String, ? extends Region> contentIndex()
    {
        return contentIndex;
    }

    @Override
    public Uniform cast(Object object)
    {
        if (object instanceof Uniform) return (Uniform) object;

        return null;
    }
}
