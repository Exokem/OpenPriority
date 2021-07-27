package openpriority.api.importer.base;

import openpriority.api.importer.IExtension;
import openpriority.api.importer.IResource;

public enum ImageResource implements IResource
{
    CLOSE_TASK(Section.ICON, ResourceVariant.HOVERED),
    EXPAND(Section.ICON, ResourceVariant.HOVERED, ResourceVariant.SELECTED, ResourceVariant.SELECTED_HOVERED),
    SELECTION(Section.ICON, ResourceVariant.HOVERED, ResourceVariant.SELECTED, ResourceVariant.SELECTED_HOVERED),
    ADD(Section.ICON, ResourceVariant.HOVERED),
    SUBTRACT(Section.ICON, ResourceVariant.HOVERED, ResourceVariant.DISABLED);

    private Section section;
    private Extension extension;
    private ResourceVariant[] variants = {};

    ImageResource(Section section, ResourceVariant... variants)
    {
        this.section = section;
        this.extension = Extension.PNG;
        this.variants = variants;
    }

    public Section section()
    {
        return section;
    }

    public ResourceVariant[] variants()
    {
        return variants;
    }

    @Override
    public IExtension extension()
    {
        return extension;
    }
}
