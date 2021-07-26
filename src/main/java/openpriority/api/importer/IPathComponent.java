package openpriority.api.importer;

public interface IPathComponent
{
    default boolean usesPrefix()
    {
        return prefix() != 0;
    }

    default char prefix()
    {
        return '/';
    }

    default String identifier()
    {
        return toString().toLowerCase();
    }

    static IPathComponent append(IPathComponent target, String appension)
    {
        return new IPathComponent()
        {
            @Override
            public String identifier()
            {
                return target.identifier() + appension;
            }
        };
    }

    static IPathComponent compoundJoin(IPathComponent target, IPathComponent... extensions)
    {
        return new IPathComponent()
        {
            @Override
            public String identifier()
            {
                return target.identifier() + join(true, extensions).identifier();
            }
        };
    }

    static IPathComponent join(boolean useFirstPrefix, IPathComponent... components)
    {
        StringBuilder identifier = new StringBuilder();

        for (int ix = 0; ix < components.length; ix ++)
        {
            IPathComponent component = components[ix];

            if ((useFirstPrefix || ix != 0) && component.usesPrefix()) identifier.append(component.prefix());
            identifier.append(component.identifier());
        }

        return new IPathComponent()
        {
            @Override
            public char prefix()
            {
                return 0;
            }

            @Override
            public String identifier()
            {
                return identifier.toString();
            }
        };
    }

    static IPathComponent join(IPathComponent... components)
    {
        return join(false, components);
    }
}
