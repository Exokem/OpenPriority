package xkv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import xkv.api.components.Uniform;
import xkv.api.factories.GridFactory;
import xkv.api.responsive.DynamicResizable;
import xkv.panels.home.Home;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

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
            URL url = OpenPriority.class.getClassLoader().getResource("css/" + sheet + ".css");

            try
            {
                if (url != null)
                {
                    OPIO.inff("Loading stylesheet: " + url.getPath());

                    String converted = convertURL(url);

                    scene.getStylesheets().add(converted);
                    STYLESHEETS.add(converted);
                }
            }

            catch (MalformedURLException ignored)
            {
                OPIO.warnf("Failed to convert URL '%s' while importing stylesheets", url.getPath());
            }
        }

        public static String convertURL(URL url) throws MalformedURLException
        {
            return new File(url.getPath()).toURI().toURL().toString();
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        Data.ROOT = stage;
        Data.CONTENT = GridFactory.autoUniform(10, 1, 1)
            .add(Home.PANEL, 1, 1, Priority.ALWAYS, Priority.ALWAYS);

        Scene scene = new Scene(Data.CONTENT);

        // TODO: load css

        stage.setScene(scene);
        stage.setTitle(Data.TITLE);
        stage.show();

        scene.widthProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
        scene.heightProperty().addListener((obs, prev, next) -> Platform.runLater(DynamicResizable::resizeAll));
    }
}
