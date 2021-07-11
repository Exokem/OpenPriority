package openpriority.api.components.window;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import openpriority.api.components.Uniform;
import openpriority.api.factories.GridFactory;

public class WindowControl extends Stage
{
    public static WindowControl defaultControl()
    {
        WindowControl wcon = new WindowControl();

        wcon.minimize = new Button("-");
        wcon.shift = new Button("O");
        wcon.exit = new Button("X");

        wcon.iconContainer = new ImageView();
        wcon.title = new Label("default window control");

        wcon.controlLayout = GridFactory.uniform(0, 5, 1)
            .add(wcon.iconContainer, 0, 0, Priority.NEVER)
            .add(wcon.title, 1, 0, Priority.ALWAYS)
            .add(wcon.minimize, 2, 0, Priority.NEVER)
            .add(wcon.shift, 3, 0, Priority.NEVER)
            .add(wcon.exit, 4, 0, Priority.NEVER);

        BorderPane contentPadding = new BorderPane();
        contentPadding.setPrefSize(1920, 1080);

        wcon.windowLayout = GridFactory.uniform(0, 1, 2)
            .add(wcon.controlLayout, 0, 0, Priority.ALWAYS, Priority.NEVER)
            .add(contentPadding, 0, 1, Priority.ALWAYS, Priority.ALWAYS);

        Scene scene = new Scene(wcon.windowLayout);
        wcon.setScene(scene);

        wcon.initStyle(StageStyle.TRANSPARENT);
        wcon.attachUtilities(wcon.controlLayout);

        return wcon;
    }

    private WindowControl() {}

    public Uniform windowLayout;
    public Uniform controlLayout;
    public Button minimize, shift, exit;
    public ImageView iconContainer;
    public Label title;

    private void attachUtilities(Region region)
    {
        region.setOnMousePressed(event ->
        {
            initialX = event.getScreenX();
            initialY = event.getScreenY();
        });

        region.setOnMouseDragged(event ->
        {
            Window window = region.getScene().getWindow();
            double dx = (event.getScreenX() - initialX) * 1.000;
            double dy = (event.getScreenY() - initialY) * 1.000;

            window.setX(window.getX() + dx);
            window.setY(window.getY() + dy);
            initialX = event.getScreenX();
            initialY = event.getScreenY();
        });

        region.setOnMouseMoved(event ->
        {

        });

        region.setOnMouseReleased(event ->
        {

        });
    }

    private double initialX, initialY;
}
