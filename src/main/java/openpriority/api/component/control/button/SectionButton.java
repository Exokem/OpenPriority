package openpriority.api.component.control.button;

import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import openpriority.api.css.IStyle;
import openpriority.api.css.Style;
import openpriority.api.css.Weight;
import openpriority.api.responsive.DynamicResizable;
import openpriority.api.responsive.Locale;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class SectionButton extends Button
{
    public static SectionButton unhoverable(String title, IStyle... styles)
    {
        return new SectionButton(title, styles);
    }

    public static SectionButton hoverable(String title, IStyle... styles)
    {
        SectionButton button = new SectionButton(title, styles);

        IStyle.apply(button, Style.HOVERABLE);

        return button;
    }

    private SectionButton(String title, IStyle... styles)
    {
        super(Locale.get(title));
        this.title = title;
        String translated = Locale.bind(title, this::setText);
        this.setText(translated);

        IStyle.apply(this, styles);
        IStyle.apply(this, Weight.REGULAR);
        setSelected(false);
    }

    public SectionButton bindSection(Region region, Consumer<SectionButton> applicator)
    {
        this.region = region;
        this.applicator = applicator;
        this.setOnAction((action) -> applicator.accept(this));

        return this;
    }

    private String title;
    private boolean selected = false;
    private Region region;
    private Consumer<SectionButton> applicator;

    public SectionButton setSelected(boolean selected)
    {
        this.selected = selected;
        IStyle.remove(this, this.selected ? Style.SECTION_BUTTON : Style.SECTION_BUTTON_SELECTED);
        IStyle.apply(this, this.selected ? Style.SECTION_BUTTON_SELECTED : Style.SECTION_BUTTON);
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

    public SectionButton scalePreferred(Supplier<Double> widthBasis, double widthFactor)
    {
        DynamicResizable.addListener(() -> setPrefWidth(widthBasis.get() * widthFactor));

        return this;
    }

    public SectionButton scaleMax(Supplier<Double> widthBasis, double widthFactor)
    {
        DynamicResizable.addListener(() -> setMaxWidth(widthBasis.get() * widthFactor));

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

    public void setTitle(String title)
    {
        this.setText(title);
    }
}
