package openpriority.panels;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;
import openpriority.panels.home.Home;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static openpriority.api.css.Style.*;

public final class Display
{
    public static class Data
    {
        public static final double CONSTANT = 1000.0D;
    }

    private static SectionButton ACTIVE_SECTION;
    public static Uniform CONTENT = content();

    private static void shiftSection(SectionButton control)
    {
        if (ACTIVE_SECTION != null)
        {
            ACTIVE_SECTION.setSelected(false);
            CONTENT.getChildren().remove(ACTIVE_SECTION.region());
        }

        ACTIVE_SECTION = control.setSelected(true);
        CONTENT.add(ACTIVE_SECTION.region(), 0, 1, Priority.ALWAYS, Priority.ALWAYS);
    }

    private static Uniform content()
    {
        SectionButton home = new SectionButton("section-home")
            .bindSection(Home.PANEL, Display::shiftSection)
            .adjustWidth(Scale.MINOR.adjust(Data.CONSTANT));

        SectionButton tasks = new SectionButton("section-tasks")
            .bindSection(Home.PANEL, Display::shiftSection)
            .adjustWidth(Scale.MINOR.adjust(Data.CONSTANT));

        SectionButton misc = new SectionButton("temp")
            .bindSection(Home.PANEL, Display::shiftSection)
            .adjustWidth(Scale.MINOR.adjust(Data.CONSTANT));

        Uniform sections = GridFactory.uniform(0, 3, 1, "section-button-tab")
            .add(home, 0, 0, Priority.NEVER, Priority.NEVER)
            .add(tasks, 1, 0, Priority.NEVER, Priority.NEVER)
            .add(misc, 2, 0, Priority.NEVER);

        ACTIVE_SECTION = home.setSelected(true);

        Uniform content = GridFactory.uniform(0, 1, 2, BG1)
            .add(sections, 0, 0, Priority.NEVER)
            .add(ACTIVE_SECTION.region(), 0, 1, Priority.ALWAYS, Priority.ALWAYS);

        return content;
    }
}
