package openpriority.panels.home;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.css.Style;
import openpriority.api.css.Weight;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicRectangle;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;

import static openpriority.api.css.Style.*;

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
            Button test = new Button("test_button");
            test.setMaxWidth(Double.MAX_VALUE);
            test.setOnAction((a) -> OpenPriority.updateStylesheets());

            DynamicRectangle left = DynamicRectangle.empty()
                .width(OpenPriority::width, 0.25D);

            Uniform root = GridFactory.autoUniform(0, 3, 4, BG1, DEBUG)
                .add(left, 0, 0, Priority.NEVER, Priority.NEVER)
                .add(left.copy(), 2, 0, Priority.NEVER, Priority.NEVER)
                .add(DynamicRectangle.heightOnly(OpenPriority::height, 0.05D), 1, 0, Priority.NEVER)
                .add(generalOptions(), 1, 1, Priority.SOMETIMES)
                .add(new Rectangle(0, 20), 1, 2, Priority.NEVER)
                .add(debugOptions(), 1, 3, Priority.SOMETIMES)
                ;
            root.setPrefSize(Data.WIDTH, Data.HEIGHT);

            return root;
        }

        private static Uniform generalOptions()
        {
            HoverLabel generalLabel = HoverLabel.configure("label-options-general", "gainsboro", "yellow", Weight.SEMIBOLD, Size.LARGE);

            ChoiceBox<String> localeSelect = new ChoiceBox<>();
            localeSelect.getItems().addAll(Locale.Variant.translationKeySet());
            localeSelect.setValue(Locale.get(Options.General.ACTIVE_LOCALE.get().translationKey()));
            localeSelect.setMinWidth(Scale.MINOR.adjust(Data.WIDTH * 0.75));
            IStyle.apply(localeSelect, Size.REGULAR);

            Locale.bind(() ->
            {
                localeSelect.getItems().clear();
                localeSelect.getItems().addAll(Locale.Variant.translationKeySet());
                localeSelect.setValue(Locale.get(Options.General.ACTIVE_LOCALE.get().translationKey()));
            });

            Button applyLocale = ControlFactory.button("action-apply-locale", Scale.MINOR.adjust(Data.WIDTH * 0.5), () ->
            {
                Locale.Variant next = Options.General.ACTIVE_LOCALE.get().inverseRetrieve(localeSelect.getValue());

                if (next != Options.General.ACTIVE_LOCALE.get())
                {
                    Options.General.ACTIVE_LOCALE.set(next);
                    Locale.refreshIndices();
                }
            });

            applyLocale.setMaxHeight(Double.MAX_VALUE);

            Label localeLabel = ControlFactory.label("label-language-select", Size.REGULAR, TEXT0);

            Uniform localeSelection = GridFactory.uniform(10, 3, 1)
                .add(localeLabel, 0, 0, Priority.NEVER)
                .add(localeSelect, 1, 0, Priority.SOMETIMES)
                .add(applyLocale, 2, 0, Priority.ALWAYS, Priority.ALWAYS);

            Uniform generalOptions = GridFactory.autoUniform(20, 1, 3, BG0)
                .add(generalLabel, 1, 1, Priority.ALWAYS)
                .add(localeSelection, 1, 2, Priority.ALWAYS);

            return generalOptions;
        }

        private static Uniform debugOptions()
        {
            HoverLabel debugLabel = HoverLabel.configure("label-options-debug", "gainsboro", "yellow", Weight.SEMIBOLD, Size.LARGE);

            CheckBox localeDebug = ControlFactory.checkBox("option-locale-debug");
            localeDebug.setSelected(Options.Debug.LOCALE_DEBUG.get());
            localeDebug.selectedProperty().addListener((obs, prev, next) -> Options.Debug.LOCALE_DEBUG.set(next));

            Button updateCSS = ControlFactory.button("action-update-css", Double.MAX_VALUE, OpenPriority::updateStylesheets);
            Button updateLocale = ControlFactory.button("action-update-locale", Double.MAX_VALUE, Locale::refreshIndices);

            Uniform debugActions = GridFactory.uniform(10, 2, 1)
                .add(updateCSS, 0, 1, Priority.SOMETIMES)
                .add(updateLocale, 2, 1, Priority.SOMETIMES);

            Uniform debugOptions = GridFactory.autoUniform(20, 1, 3, BG0, DEBUG)
                .add(debugLabel, 1, 1, Priority.ALWAYS)
                .add(localeDebug, 1, 2, Priority.ALWAYS)
                .add(debugActions, 1, 3, Priority.ALWAYS);

            return debugOptions;
        }
    }
}
