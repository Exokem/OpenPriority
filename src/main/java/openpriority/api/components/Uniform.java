package openpriority.api.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.shape.Rectangle;
import openpriority.api.responsive.IDynamicRegion;

import java.util.Arrays;
import java.util.Collection;

public class Uniform extends GridPane implements IDynamicRegion<Uniform>
{
    private int columns = 0, rows = 0;

    public Uniform(int columns, int rows)
    {
        this();

        this.columns = columns;
        this.rows = rows;

        pad(columns, rows);
    }

    public Uniform()
    {
        super();
        setSnapToPixel(true);
    }

    @Deprecated
    protected Uniform pad(int columns, int rows)
    {
        for (int col = 0; col < columns; col++)
        {
            for (int row = 0; row < rows; row++)
            {
                Rectangle r = new Rectangle(0, 0);
                add(r, col, row);
            }
        }

        return this;
    }

    public Uniform inset(double inset)
    {
        Insets insets = new Insets(inset);

        setPadding(insets);

        return this;
    }

    public Uniform percentWidth(double percent)
    {
        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(percent);
        return this.addColumnConstraints(constraints);
    }

    public Uniform percentHeight(double percent)
    {
        RowConstraints constraints = new RowConstraints();
        constraints.setPercentHeight(percent);
        return this.addRowConstraints(constraints);
    }

    public Uniform addColumnConstraints(ColumnConstraints... constraints)
    {
        this.getColumnConstraints().addAll(Arrays.asList(constraints));
        return this;
    }

    public Uniform addColumnConstraints(Collection<ColumnConstraints> constraints)
    {
        this.getColumnConstraints().addAll(constraints);
        return this;
    }

    public Uniform addRowConstraints(RowConstraints... constraints)
    {
        this.getRowConstraints().addAll(Arrays.asList(constraints));
        return this;
    }

    public Uniform addRowConstraints(Collection<RowConstraints> constraints)
    {
        this.getRowConstraints().addAll(constraints);
        return this;
    }

    public Uniform add(Node child, int column, int row, Priority horizontal, Priority vertical)
    {
        return add(child, column, row, 1, 1, horizontal, vertical);
    }

    public Uniform add(Node child, int column, int row, Priority... expansions)
    {
        return add(child, column, row, 1, 1, expansions);
    }

    public Uniform add(Node child, int column, int row, int colSpan, int rowSpan, Priority horizontal, Priority vertical)
    {
        super.add(child, column, row, colSpan, rowSpan);

        if (horizontal != null) GridPane.setHgrow(child, horizontal);
        if (vertical != null) GridPane.setVgrow(child, vertical);

        columns = Math.max(columns, column + 1);
        rows = Math.max(rows, row + 1);

        return this;
    }

    @Deprecated
    @Override
    public final void add(Node child, int columnIndex, int rowIndex)
    {
        super.add(child, columnIndex, rowIndex);
    }

    @Deprecated
    @Override
    public final void add(Node child, int columnIndex, int rowIndex, int colspan, int rowspan)
    {
        super.add(child, columnIndex, rowIndex, colspan, rowspan);
    }

    @Deprecated
    @Override
    public final void addRow(int rowIndex, Node... children)
    {
        super.addRow(rowIndex, children);
    }

    @Deprecated
    @Override
    public final void addColumn(int columnIndex, Node... children)
    {
        super.addColumn(columnIndex, children);
    }

    public Uniform add(Node child, int column, int row, int colSpan, int rowSpan, Priority... expansions)
    {
        Priority horizontal = null, vertical = null;

        try
        {
            horizontal = expansions[0];
            vertical = expansions[1];
        } catch (ArrayIndexOutOfBoundsException ignored)
        {

        }

        add(child, column, row, colSpan, rowSpan, horizontal, vertical);

        return this;
    }

    public Uniform withMaxWidth(double width)
    {
        this.setMaxWidth(width);
        return this;
    }

    public int columns()
    {
        return columns;
    }

    public int rows()
    {
        return rows;
    }

    @Override
    public Uniform cast(Object object)
    {
        if (object instanceof Uniform) return (Uniform) object;

        return null;
    }
}
