package openpriority.api.responsive;

import javafx.application.Platform;
import javafx.scene.layout.Region;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class DynamicResizable
{
    private static final Set<Runnable> LISTENERS = new HashSet<>();

    public static void resizeAll()
    {
        LISTENERS.forEach(Runnable::run);
    }

    public static void addListener(Runnable listener)
    {
        Platform.runLater(listener);
        LISTENERS.add(listener);
    }

    /**
     * Delays the introduction of the provided listener {@link Runnable} until the provided condition is met.
     *
     * Delaying the initial application of a resizable function may be useful when it depends on a value that has not
     * yet been set correctly, as in the case where a component A whose dimensions impact another component B will have
     * width and height 0 before it is completely added and visible.
     *
     * @param listener The {@link Runnable} resize function.
     * @param condition The condition that must be met before the resize function can be applied initially.
     */
    public static void addDelayedListener(Runnable listener, Supplier<Boolean> condition)
    {
        Platform.runLater(() ->
        {
            if (!condition.get())
            {
                Platform.runLater(() -> addDelayedListener(listener, condition));
            }

            else
            {
                listener.run();
                LISTENERS.add(listener);
            }
        });
    }

    public static void addWidthListener(Region region, Supplier<Double> sizeProvider)
    {
        addListener(() ->
        {
            region.setPrefWidth(sizeProvider.get());
            region.setMaxWidth(sizeProvider.get());
        });
    }

    public static void addDynamicListener(Supplier<Double> provider, Consumer<Double> receiver)
    {
        addListener(() -> receiver.accept(provider.get()));
    }
}
