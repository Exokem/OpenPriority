package openpriority.api.factory;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class TransitionFactory
{
    public static final double FADE_DURATION = 450;

    public static void applyHoverFade(Node hovered, Node overlay, Node base, boolean boundHover)
    {
        FadeTransition overlayFade = new FadeTransition();
        overlayFade.setDuration(Duration.millis(FADE_DURATION));
        overlayFade.setToValue(1.0D);
        overlayFade.setFromValue(0.0D);
        overlayFade.setNode(overlay);

        FadeTransition baseFade = new FadeTransition();
        baseFade.setDuration(Duration.millis(FADE_DURATION));
        baseFade.setToValue(0.0D);
        baseFade.setFromValue(1.0D);
        baseFade.setNode(base);

        overlay.setOpacity(0.0D);
        hovered.setPickOnBounds(boundHover);

        hovered.hoverProperty().addListener((v, wsHov, isHov) ->
        {
            overlayFade.setRate(isHov ? 1.0D : -1.0D);
            baseFade.setRate(isHov ? 1.0D : -1.0D);

            overlayFade.play();
            baseFade.play();
        });
    }
}
