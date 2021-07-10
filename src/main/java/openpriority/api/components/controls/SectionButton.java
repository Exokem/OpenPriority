package openpriority.api.components.controls;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import openpriority.api.css.Style;
import openpriority.api.css.StyleApplicator;
import openpriority.api.responsive.Locale;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class SectionButton extends Button
{
    public SectionButton(String title)
    {
        super(Locale.get(title));
        this.title = title;
        setSelected(false);
    }

    public SectionButton bindSection(Region region, Consumer<SectionButton> applicator)
    {
        this.region = region;
        this.applicator = applicator;
        this.setOnAction((action) -> applicator.accept(this));

        return this;
    }

    private final String title;
    private boolean selected = false;
    private Region region;
    private Consumer<SectionButton> applicator;

    public SectionButton setSelected(boolean selected)
    {
        this.selected = selected;
        Style.remove(this, this.selected ? Style.SECTION_BUTTON : Style.SECTION_BUTTON_SELECTED);
        Style.apply(this, this.selected ? Style.SECTION_BUTTON_SELECTED : Style.SECTION_BUTTON);
        return this;
    }

    public SectionButton limitWidth(double width)
    {
        this.setMaxWidth(width);
        return this;
    }

    public SectionButton adjustWidth(double width)
    {
        this.setPrefWidth(width);
        return this;
    }

    public Region region()
    {
        return region;
    }

    public boolean matches(SectionButton other)
    {
        return other.title.equals(title);
    }
}
