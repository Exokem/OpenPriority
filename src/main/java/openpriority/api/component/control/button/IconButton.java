package openpriority.api.component.control.button;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import openpriority.api.component.layout.Uniform;
import openpriority.api.factory.TransitionFactory;
import openpriority.api.importer.base.ImageResource;
import openpriority.api.importer.base.ResourceVariant;
import openpriority.internal.ImageController;

public class IconButton extends Uniform
{
    public IconButton(ImageResource imageProvider)
    {
        super();

        this.imageProvider = imageProvider;
        base = new ImageView(ImageController.get(imageProvider));
        hovered = new ImageView(ImageController.getVariant(imageProvider, ResourceVariant.HOVERED));

        base.setPreserveRatio(true);
        hovered.setPreserveRatio(true);

        base.setFitWidth(1);
        hovered.setFitWidth(1);

        add(base, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
        add(hovered, 0, 0, Priority.ALWAYS, Priority.ALWAYS);

        TransitionFactory.applyHoverFade(this, hovered, base, true);
    }

    private ImageView base = new ImageView(), hovered = new ImageView();

    private ImageResource imageProvider;

    public IconButton bindImage(ImageResource imageProvider)
    {
        base.setImage(ImageController.get(imageProvider));
        hovered.setImage(ImageController.getVariant(imageProvider, ResourceVariant.HOVERED));

        this.imageProvider = imageProvider;
        return this;
    }

    public void resizeSquare(double dimension)
    {
        requireHeight(dimension);

        Platform.runLater(() ->
        {
            base.resize(dimension, dimension);
            hovered.resize(dimension, dimension);

            base.setFitHeight(dimension);
            base.setFitWidth(dimension);
            hovered.setFitHeight(dimension);
            hovered.setFitWidth(dimension);
        });
    }
}
