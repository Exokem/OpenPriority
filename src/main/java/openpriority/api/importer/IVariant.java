package openpriority.api.importer;

public interface IVariant extends IPathComponent
{
    @Override
    default char prefix()
    {
        return '_';
    }
}
