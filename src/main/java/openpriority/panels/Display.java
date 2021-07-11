package openpriority.panels;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import openpriority.OpenPriority;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.SectionButton;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.Scale;
import openpriority.panels.home.Home;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static openpriority.api.css.Style.BG1;

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
        CONTENT.add(ACTIVE_SECTION.region(), 0, 2, Priority.ALWAYS, Priority.ALWAYS);
    }

    private static Uniform content()
    {
        SectionButton time = Scale.scaleMinWidth(SectionButton.unhoverable(""),
            OpenPriority::width, Scale.MINOR.quarter());
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        final Timeline timeline = new Timeline
        (
            new KeyFrame
            (
                Duration.millis(500),
                event -> time.setText(timeFormat.format(System.currentTimeMillis()))
            )
        );

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        Uniform information = GridFactory.uniform(0, 2, 1)
            .add(SectionButton.unhoverable("").limitWidth(Double.MAX_VALUE), 0, 0, Priority.SOMETIMES)
            .add(time, 1, 0, Priority.NEVER);

        SectionButton home = Scale.scaleMaxWidth(SectionButton.hoverable("section-home")
            .bindSection(Home.PANEL, Display::shiftSection),
            OpenPriority::width, Scale.MINOR.half());

        SectionButton tasks = Scale.scaleMaxWidth(SectionButton.hoverable("section-tasks")
            .bindSection(Home.PANEL, Display::shiftSection),
            OpenPriority::width, Scale.MINOR.half());

        SectionButton empty = SectionButton.unhoverable("")
            .limitWidth(Double.MAX_VALUE);

        Uniform sections = GridFactory.uniform(0, 3, 1)
            .add(home, 0, 0, Priority.NEVER, Priority.NEVER)
            .add(tasks, 1, 0, Priority.NEVER, Priority.NEVER)
            .add(empty, 2, 0, Priority.SOMETIMES);

        ACTIVE_SECTION = home.setSelected(true);

        Uniform content = GridFactory.uniform(0, 1, 3, BG1)
            .add(information, 0, 0, Priority.ALWAYS)
            .add(sections, 0, 1, Priority.ALWAYS)
            .add(ACTIVE_SECTION.region(), 0, 2, Priority.ALWAYS, Priority.ALWAYS);

        return content;
    }
}
