package openpriority;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import openpriority.api.components.Uniform;
import openpriority.api.importers.GeneralImporter;
import openpriority.api.responsive.DynamicResizable;
import openpriority.api.responsive.Locale;
import openpriority.panels.Display;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class OpenPriority extends Application
{
    private static final class Data
    {
        private static final String TITLE = "OpenPriority";
        private static final Set<String> STYLESHEETS = new HashSet<>();

        private static Stage OPEN_PRIORITY;
        private static Uniform CONTENT;

        private static void importStylesheet(Scene scene, String sheet)
        {
            Consumer<String> importer = (path) ->
            {
                scene.getStylesheets().add(path);
                STYLESHEETS.add(path);
            };

            if (GeneralImporter.convertedImport("css/", sheet, ".css", importer))
            {
                OPIO.inff("Imported stylesheet '%s'", sheet);
            }

            else
            {
                OPIO.warnf("Failed to import stylesheet '%s'", sheet);
            }
        }
    }

    public static void refreshStylesheets()
    {
        Data.OPEN_PRIORITY.getScene().getStylesheets().clear();
        Data.OPEN_PRIORITY.getScene().getStylesheets().addAll(Data.STYLESHEETS);
        OPIO.inff("Refreshed active stylesheets");
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        // TODO: read config from file

        Locale.configureLocale("en_uk");

        Data.OPEN_PRIORITY = stage;
        Data.CONTENT = Display.CONTENT;

        Scene scene = new Scene(Data.CONTENT);

        Data.importStylesheet(scene, "fonts");
        Data.importStylesheet(scene, "colors");
        Data.importStylesheet(scene, "panels");
        Data.importStylesheet(scene, "controls");

        stage.setScene(scene);

        stage.setTitle(Data.TITLE);
        stage.show();

        scene.widthProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
        scene.heightProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
    }

    public static double width()
    {
        return Data.OPEN_PRIORITY.getWidth();
    }

    public static double height()
    {
        return Data.OPEN_PRIORITY.getHeight();
    }
}
