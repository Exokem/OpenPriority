package openpriority.panels.home;

import openpriority.api.components.Uniform;
import openpriority.api.factories.GridFactory;

public final class HomePanel
{
    public static final Uniform PANEL = Components.panel();

    private static final class Data
    {

    }

    private static final class Components
    {
        private static Uniform panel()
        {
            Uniform panel = GridFactory.autoUniform(0, 0, 0);

            return panel;
        }
    }
}
