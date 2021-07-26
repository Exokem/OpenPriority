package openpriority.api.factory;

import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.Uniform;

public final class BaseUniformBuilder extends UniformBuilder<Uniform>
{
    public static BaseUniformBuilder start(Alignment alignment)
    {
        return new BaseUniformBuilder(alignment);
    }

    private BaseUniformBuilder(Alignment axis)
    {
        super(Uniform::new, axis);
    }
}
