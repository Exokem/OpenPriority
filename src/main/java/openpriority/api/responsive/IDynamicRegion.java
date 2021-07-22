package openpriority.api.responsive;

import javafx.scene.layout.Region;
import openpriority.api.exception.InvalidInheritanceException;

import java.util.function.Supplier;

public interface IDynamicRegion<V extends Region>
{
    V cast(Object object);

    default V requireWidth(Supplier<Double> basis, double factor)
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            DynamicResizable.addListener(() -> region.setMinWidth(basis.get() * factor));
            DynamicResizable.addListener(() -> region.setMaxWidth(basis.get() * factor));

            return cast(this);
        }

        else throw new InvalidInheritanceException(this.getClass(), Region.class, IDynamicRegion.class);
    }

    default V limitWidth(Supplier<Double> basis, double factor)
    {
        if (this instanceof Region)
        {
            Region region = (Region) this;

            DynamicResizable.addListener(() -> region.setMaxWidth(basis.get() * factor));

            return cast(this);
        }

        else throw new InvalidInheritanceException(this.getClass(), Region.class, IDynamicRegion.class);
    }
}
