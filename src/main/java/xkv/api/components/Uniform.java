package xkv.api.components;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import xkv.api.factories.GridFactory;

public class Uniform extends GridPane
{
    public Uniform(int columns, int rows)
    {
        super();
        pad(columns, rows);
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
        super.add(child, column, row);

        if (horizontal != null) GridPane.setHgrow(child, horizontal);
        if (vertical != null) GridPane.setVgrow(child, vertical);

        return this;
    }

    public Uniform add(Node child, int column, int row, Priority... expansions)
    {
        Priority horizontal = null, vertical = null;

        try
        {
            horizontal = expansions[0];
            vertical = expansions[1];
        } catch (ArrayIndexOutOfBoundsException ignored)
        {

        }

        add(child, column, row, horizontal, vertical);

        return this;
    }
}
