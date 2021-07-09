package xkv.panels.home;

import javafx.scene.control.Button;
import javafx.scene.layout.Priority;
import xkv.api.components.Uniform;
import xkv.api.css.Style;
import xkv.api.factories.GridFactory;

public final class Home
{
    public static final Uniform PANEL = Components.root();

    private static final class Data
    {
        private static final double WIDTH = 1920, HEIGHT = 1080;
    }

    private static final class Components
    {
        private static Uniform root()
        {
            Uniform root = GridFactory.autoUniform(10, 1, 1, "debug");

            root.setPrefSize(Data.WIDTH, Data.HEIGHT);

            Button test = new Button("test_button");

            Style.apply(test, "test-style");

            root.add(test, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

            return root;
        }
    }
}
