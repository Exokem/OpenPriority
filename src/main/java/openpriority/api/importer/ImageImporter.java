package openpriority.api.importer;

import javafx.scene.image.Image;
import openpriority.api.importer.base.Extension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageImporter
{
    public enum DataHeader implements IPathComponent
    {
        RESOURCES;

        @Override
        public char prefix()
        {
            return 0;
        }
    }

    public static Image retrieveImage(IPathComponent... pathComponents)
    {
        if (pathComponents.length == 1)
        {
            return retrieveImage(IPathComponent.compoundJoin(DataHeader.RESOURCES, pathComponents[0]).identifier());
        }

        List<IPathComponent> components = new ArrayList<>(List.of(DataHeader.RESOURCES));
        components.addAll(Arrays.asList(pathComponents));

        return retrieveImage(IPathComponent.join(components.toArray(new IPathComponent[components.size()])).identifier());
    }

    public static Image retrieveImage(ISection section, IResource resource, IVariant variant)
    {
        return retrieveImage(IPathComponent.join(DataHeader.RESOURCES, section, resource, variant, Extension.PNG).identifier());
    }

    public static Image retrieveImage(String path)
    {
        try
        {
            return new Image(GeneralImporter.genericRetrieve(path).toURI().toURL().toString());
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
