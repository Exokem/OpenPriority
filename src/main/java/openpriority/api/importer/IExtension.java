package openpriority.api.importer;

public interface IExtension extends IPathComponent
{
    @Override
    default char prefix()
    {
        return '.';
    }
}
