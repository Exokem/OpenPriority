package openpriority.api.factories;

import openpriority.api.components.layouts.Alignment;
import openpriority.api.components.layouts.Uniform;

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
