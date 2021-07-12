package openpriority.api.components.controls;

import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class UniformScrollPane extends ScrollPane
{
    public UniformScrollPane(Node content)
    {
        setFitToWidth(true);
        setFitToHeight(true);
        setSnapToPixel(true);

        contentProperty().addListener(((obs, prev, next) ->
        {
            if (next instanceof Region)
            {
                Region region = (Region) next;

                setFitToHeight(region.getHeight() <= getHeight());
            }
        }));

        setContent(content);
    }
}
