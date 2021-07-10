package openpriority.panels.home;

import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.components.Uniform;
import openpriority.api.css.Style;
import openpriority.api.factories.GridFactory;

import static openpriority.api.css.Style.*;

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
            Uniform root = GridFactory.autoUniform(10, 1, 1, BG0);

            root.setPrefSize(Data.WIDTH, Data.HEIGHT);

            Button test = new Button("test_button");

            test.setOnAction((a) -> OpenPriority.refreshStylesheets());

            Style.apply(test, "test-style");

            root.add(test, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

            return root;
        }
    }
}
