package xkv.api.responsive;

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
