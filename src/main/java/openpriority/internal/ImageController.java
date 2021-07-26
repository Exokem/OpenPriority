package openpriority.internal;

import javafx.scene.image.Image;
import openpriority.OPIO;
import openpriority.api.importer.ImageImporter;
import openpriority.api.importer.base.ImageResource;
import openpriority.api.importer.base.ResourceVariant;

import java.util.HashMap;
import java.util.Map;

public class ImageController
{
    private static final Map<ImageResource, Image> IMPORTED_IMAGES = new HashMap<>();
    private static final Map<ImageResource, Map<ResourceVariant, Image>> IMPORTED_VARIANT_IMAGES = new HashMap<>();

    public static Image get(ImageResource resource)
    {
        return IMPORTED_IMAGES.get(resource);
    }

    public static Image getVariant(ImageResource resource, ResourceVariant variant)
    {
        if (!IMPORTED_VARIANT_IMAGES.containsKey(resource)) return null;

        return IMPORTED_VARIANT_IMAGES.get(resource).getOrDefault(variant, null);
    }

    public static void retrieveImageResources()
    {
        for (ImageResource resource : ImageResource.values())
        {
            Image image;

            image = ImageImporter.retrieveImage(resource.section(), resource, resource.extension());

            if (image != null) IMPORTED_IMAGES.put(resource, image);
            else OPIO.warnf("Failed to import image (unspecified variant)");

            if (resource.variants().length != 0)
            {
                Map<ResourceVariant, Image> variantMappings = new HashMap<>();

                if (IMPORTED_VARIANT_IMAGES.containsKey(resource)) variantMappings = IMPORTED_VARIANT_IMAGES.get(resource);

                for (ResourceVariant variant : resource.variants())
                {
                    image = ImageImporter.retrieveImage(resource.section(), resource, variant, resource.extension());
                    variantMappings.put(variant, image);
                }

                IMPORTED_VARIANT_IMAGES.put(resource, variantMappings);
            }
        }
    }
}
