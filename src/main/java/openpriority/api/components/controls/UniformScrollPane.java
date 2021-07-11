package openpriority.api.components.controls;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Region;

public class UniformScrollPane extends ScrollPane
{
    public UniformScrollPane(Node content)
    {
        setFitToWidth(true);
        setSnapToPixel(true);

        contentProperty().addListener(((obs, prev, next) ->
        {
            if (next instanceof Region)
            {
                Region region = (Region) next;

                if (region.getHeight() < getHeight())
                {
                    setFitToHeight(true);
                }

                else setFitToHeight(false);
            }
        }));

        if (content instanceof Region)
        {
            Region region = (Region) content;

            Platform.runLater(() ->
            {
                setFitToHeight(region.getHeight() < getHeight());
            });
        }

        setContent(content);
    }
}
