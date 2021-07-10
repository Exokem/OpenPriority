package openpriority.api.components;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;

public class Uniform extends GridPane
{
    public Uniform(int columns, int rows)
    {
        this();
        pad(columns, rows);
    }

    public Uniform()
    {
        super();
        setSnapToPixel(true);
    }

    protected Uniform pad(int columns, int rows)
    {
        for (int col = 0; col < columns; col++)
        {
            for (int row = 0; row < rows; row++)
            {
                Rectangle r = new Rectangle(0, 0);
                this.add(r, col, row);
            }
        }

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

        return this;
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
}
