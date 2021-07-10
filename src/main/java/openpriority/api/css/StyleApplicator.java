package openpriority.api.css;

import javafx.scene.layout.Region;

import java.util.Arrays;
import java.util.Collection;

public final class StyleApplicator
{
    public static void addStyle(Region region, Style... styles)
    {
        addStyle(region, Arrays.asList(styles));
    }

    public static void addStyle(Region region, Collection<Style> styles)
    {
        styles.forEach(style -> region.getStyleClass().add(style.css()));
    }
}
