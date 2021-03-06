package openpriority.api.component.control.button;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import openpriority.api.component.layout.Uniform;
import openpriority.api.factory.TransitionFactory;
import openpriority.api.importer.base.ImageResource;
import openpriority.api.importer.base.ResourceVariant;
import openpriority.api.responsive.DynamicResizable;
import openpriority.internal.ImageController;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class IconButton extends Uniform
{
    public IconButton(ImageResource imageProvider)
    {
        super();

        base.setPreserveRatio(true);
        hovered.setPreserveRatio(true);
        disabled.setPreserveRatio(true);

        base.setFitWidth(1);
        hovered.setFitWidth(1);
        disabled.setFitWidth(1);

        add(base, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
        add(hovered, 0, 0, Priority.ALWAYS, Priority.ALWAYS);
        add(disabled, 0, 0, Priority.ALWAYS, Priority.ALWAYS);

        bindImage(imageProvider);

        functionDisabledProperty.set(false);
    }

    private ImageView base = new ImageView(), hovered = new ImageView(), disabled = new ImageView();
    private ImageResource imageProvider;

    private final BooleanProperty selectedProperty = new SimpleBooleanProperty(false);
    private final BooleanProperty functionDisabledProperty = new SimpleBooleanProperty(false);

    public IconButton bindSelectionFunction(Consumer<Boolean> selectionFunction)
    {
        selectedProperty.addListener((v, r, selected) -> selectionFunction.accept(selected));
        return this;
    }

    public IconButton bindImage(ImageResource imageProvider)
    {
        Image baseImage = ImageController.get(imageProvider),
            hoveredImage = ImageController.getVariant(imageProvider, ResourceVariant.HOVERED),
            selectedImage = ImageController.getVariant(imageProvider, ResourceVariant.SELECTED),
            selectedHoveredImage = ImageController.getVariant(imageProvider, ResourceVariant.SELECTED_HOVERED),
            disabledImage = ImageController.getVariant(imageProvider, ResourceVariant.DISABLED);

        base.setImage(baseImage);

        if (hoveredImage != null)
        {
            hovered.setImage(hoveredImage);
            TransitionFactory.applyHoverFade(this, hovered, base, true);
        }

        Runnable selectionEvent = () -> selectedProperty.set(!selectedProperty.get());

        if (disabledImage != null)
        {
            disabled.setImage(disabledImage);

            functionDisabledProperty.addListener((v, r, disabled) ->
            {
                setOnMouseClicked(disabled ? value -> {} : value -> selectionEvent.run());
                this.disabled.setVisible(disabled);
                this.base.setVisible(!disabled);
                this.hovered.setVisible(!disabled);
            });
        }

        setOnMouseClicked(value -> selectionEvent.run());

        selectedProperty.addListener((v, r, selected) ->
        {
            if (selectedImage != null && selectedHoveredImage != null)
            {
                base.setImage(selected ? selectedImage : baseImage);
                hovered.setImage(selected ? selectedHoveredImage : hoveredImage);
            }
        });

        this.imageProvider = imageProvider;
        return this;
    }

    public IconButton autoResize(Supplier<Double> dimensionProvider)
    {
        DynamicResizable.addDelayedListener(() -> resizeSquare(dimensionProvider.get()), () -> dimensionProvider.get() != 0);
        return this;
    }

    public void resizeSquare(double dimension)
    {
        requireHeight(dimension);

        Platform.runLater(() ->
        {
            base.setFitHeight(dimension);
            base.setFitWidth(dimension);
            hovered.setFitHeight(dimension);
            hovered.setFitWidth(dimension);
            disabled.setFitHeight(dimension);
            disabled.setFitWidth(dimension);
        });
    }

    public BooleanProperty getSelectedProperty()
    {
        return selectedProperty;
    }

    public BooleanProperty getFunctionDisabledProperty() { return functionDisabledProperty; }
}
