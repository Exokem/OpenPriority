package openpriority.panels;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.components.controls.UniformScrollPane;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.Scale;
import openpriority.panels.home.HomePanel;
import openpriority.panels.options.OptionPanel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static openpriority.api.css.Style.BG1;

public final class Display
{
    public static class Data
    {
        public static final double CONSTANT = 1000.0D;
        private static final double WIDTH = 1920, HEIGHT = 1080;
    }

    private static SectionButton ACTIVE_SECTION;
    private static UniformScrollPane SECTION_CONTAINER;
    public static Uniform CONTENT = content();

    private static class Components
    {
        private static Uniform INFORMATION;
        private static SectionButton TIME;
    }

    private static void shiftSection(SectionButton control)
    {
        if (ACTIVE_SECTION != null)
        {
            ACTIVE_SECTION.setSelected(false);
//            CONTENT.getChildren().remove(ACTIVE_SECTION.region());
        }

        ACTIVE_SECTION = control.setSelected(true);
        SECTION_CONTAINER.setContent(ACTIVE_SECTION.region());
//        CONTENT.add(ACTIVE_SECTION.region(), 0, 2, Priority.ALWAYS, Priority.ALWAYS);
    }

    public static void applyInformationDisplaySetting()
    {
        if (Options.Interface.SHOW_INFORMATION.get()) CONTENT.add(Components.INFORMATION, 0, 0, Priority.ALWAYS);
        else CONTENT.getChildren().remove(Components.INFORMATION);
    }

    public static void applyTimeDisplaySetting()
    {
        if (Options.Interface.SHOW_TIME.get()) Components.INFORMATION.add(Components.TIME, 1, 0, Priority.NEVER);
        else Components.INFORMATION.getChildren().remove(Components.TIME);
    }

    private static Uniform content()
    {
        Components.TIME = Scale.scaleMinWidth(SectionButton.unhoverable(""),
            OpenPriority::width, Scale.MINOR.half());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final Timeline timeline = new Timeline
        (
            new KeyFrame
            (
                Duration.millis(500),
                event -> Components.TIME.setText(timeFormat.format(System.currentTimeMillis()))
            )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Components.INFORMATION = GridFactory.uniform(0, 2, 1)
            .add(SectionButton.unhoverable("").limitWidth(Double.MAX_VALUE), 0, 0, Priority.SOMETIMES);

        if (Options.Interface.SHOW_TIME.get())
            Components.INFORMATION.add(Components.TIME, 1, 0, Priority.NEVER);

        SectionButton home = Scale.scaleMaxWidth(SectionButton.hoverable("section-home")
            .bindSection(HomePanel.PANEL, Display::shiftSection),
            OpenPriority::width, Scale.MINOR.half());

        SectionButton options = Scale.scaleMinWidth(SectionButton.hoverable("section-options")
            .bindSection(OptionPanel.PANEL, Display::shiftSection),
            OpenPriority::width, Scale.MINOR.half());

        SectionButton tasks = Scale.scaleMaxWidth(SectionButton.hoverable("section-tasks")
            .bindSection(OptionPanel.PANEL, Display::shiftSection),
            OpenPriority::width, Scale.MINOR.half());

        SectionButton empty = SectionButton.unhoverable("")
            .limitWidth(Double.MAX_VALUE);

        Uniform sections = GridFactory.uniform(0, 4, 1)
            .add(home, 0, 0, Priority.NEVER, Priority.NEVER)
            .add(tasks, 1, 0, Priority.NEVER, Priority.NEVER)
            .add(empty, 2, 0, Priority.SOMETIMES)
            .add(options, 3, 0, Priority.NEVER);

        ACTIVE_SECTION = home.setSelected(true);

        SECTION_CONTAINER = Scale.preferSize(new UniformScrollPane(ACTIVE_SECTION.region()), Data.WIDTH, Data.HEIGHT);

        Uniform content = GridFactory.uniform(0, 1, 3, BG1)
            .add(sections, 0, 1, Priority.ALWAYS)
            .add(SECTION_CONTAINER, 0, 2, Priority.ALWAYS, Priority.ALWAYS);

        if (Options.Interface.SHOW_INFORMATION.get()) content.add(Components.INFORMATION, 0, 0, Priority.ALWAYS);

        return content;
    }
}
