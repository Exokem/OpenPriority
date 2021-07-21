package openpriority.api.components;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;

public class Uniform extends GridPane
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
}
