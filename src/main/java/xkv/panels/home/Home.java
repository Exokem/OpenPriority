package xkv.panels.home;

import xkv.api.components.Uniform;
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
            Uniform root = GridFactory.autoUniform(10, 1, 1);

            root.setPrefSize(Data.WIDTH, Data.HEIGHT);

            return root;
        }
    }
}
