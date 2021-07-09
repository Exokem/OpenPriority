package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.api.components.Uniform;
import xkv.api.factories.GridFactory;
import xkv.api.importers.GeneralImporter;
import xkv.api.responsive.DynamicResizable;
import xkv.api.responsive.Locale;
import xkv.panels.home.Home;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class OpenPriority extends Application
{
    private static final class Data
    {
        private static final String TITLE = "OpenPriority";
        private static final Set<String> STYLESHEETS = new HashSet<>();

        private static Stage ROOT;
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

    public static void applyStylesheet(String path)
    {
        Data.ROOT.getScene().getStylesheets().add(path);
        Data.STYLESHEETS.add(path);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        // TODO: read config from file

        Locale.configureLocale("en_uk");

        Data.ROOT = stage;
        Data.CONTENT = GridFactory.autoUniform(10, 1, 1)
            .add(Home.PANEL, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        Scene scene = new Scene(Data.CONTENT);

        Data.importStylesheet(scene, "fonts");

        stage.setScene(scene);

        stage.setTitle(Data.TITLE);
        stage.show();

        scene.widthProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
        scene.heightProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
    }
}
