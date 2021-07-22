package openpriority.api.responsive;

import javafx.scene.layout.Region;
import openpriority.OPIO;
import openpriority.api.exception.InvalidInheritanceException;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public interface IDynamicRegion<V extends Region>
{
    V cast(Object object);

    default Region toRegion()
    {
        if (this instanceof Region) return (Region) this;
        else throw new InvalidInheritanceException(this.getClass(), Region.class, IDynamicRegion.class);
    }

    default V requireWidth(Supplier<Double> basis, double factor)
    {
        Region region = toRegion();

        DynamicResizable.addListener(() -> region.setMinWidth(basis.get() * factor));
        DynamicResizable.addListener(() -> region.setMaxWidth(basis.get() * factor));

        return cast(this);
    }

    default V limitWidth(Supplier<Double> basis, double factor)
    {
        Region region = toRegion();

        DynamicResizable.addListener(() -> region.setMaxWidth(basis.get() * factor));

        return cast(this);
    }

    default V invokeSizeFunction(SizeFunction function, double value)
    {
        Region region = toRegion();

        function.safeInvoke(region, value);

        return cast(this);
    }

    enum SizeFunction
    {
        SET_MIN_WIDTH("setMinWidth"), SET_PREF_WIDTH("setPrefWidth"), SET_MAX_WIDTH("setMaxWidth"),
        SET_MIN_HEIGHT("setMinHeight"), SET_PREF_HEIGHT("setPrefHeight"), SET_MAX_HEIGHT("setMaxHeight")
        ;

        private final String function;

        SizeFunction(String function)
        {
            this.function = function;
        }

        public void safeInvoke(Region region, double value)
        {
            switch (this)
            {
                case SET_MIN_WIDTH -> region.setMinWidth(value);
                case SET_PREF_WIDTH -> region.setPrefWidth(value);
                case SET_MAX_WIDTH -> region.setMaxWidth(value);
                case SET_MIN_HEIGHT -> region.setMinHeight(value);
                case SET_PREF_HEIGHT -> region.setPrefHeight(value);
                case SET_MAX_HEIGHT -> region.setMaxHeight(value);
            }
        }

        public void invoke(Region region, double value)
        {
            try
            {
                Region.class.getMethod(this.function, Double.class).invoke(region, value);
            }

            catch (IllegalAccessException e)
            {
                OPIO.warnf
                    (
                        "Failed to access function '%s' for object '%s': %s",
                        function, region.toString(), e.getMessage()
                    );

                e.printStackTrace();
            }

            catch (InvocationTargetException e)
            {
                OPIO.warnf
                (
                    "An exception occurred while attempting to invoke the function '%s' on object '%s': %s",
                    function, region.toString(), e.getMessage()
                );

                e.printStackTrace();
            }

            catch (NoSuchMethodException e)
            {
                OPIO.warnf
                (
                    "Function '%s' not found for Region '%s': %s",
                    function, region.toString(), e.getMessage()
                );

                e.printStackTrace();
            }
        }
    }
}
