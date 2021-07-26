package openpriority.api.factories;

import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;

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
