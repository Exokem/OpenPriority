package openpriority.panels.options;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.css.Weight;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicInsets;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;
import openpriority.panels.Display;
import openpriority.panels.UniformMargins;

import static openpriority.api.css.Style.*;

public final class OptionPanel
{
    public static final Uniform PANEL = Components.panel();

    private static final class Components
    {
        private static Uniform panel()
        {
            Uniform root = GridFactory.autoUniform(0, 3, 2, BG1)
                .add(UniformMargins.defaultMarginSide(), 0, 0, Priority.NEVER, Priority.NEVER)
                .add(UniformMargins.defaultMarginSide(), 2, 0, Priority.NEVER, Priority.NEVER)
                .add(UniformMargins.defaultMarginTop(), 1, 0, Priority.NEVER)

                .add(allOptions(), 1, 1, Priority.ALWAYS)
                ;

            return root;
        }

        private static Uniform allOptions()
        {
            HoverLabel optionsLabel = HoverLabel.configure("section-options", Color.TEXT_0, Color.ACCENT_1, Weight.BOLD, Size.LARGE_1);

            Uniform allOptions = GridFactory.autoUniform(0, 1, 7, BG1)
                .add(optionsLabel, 0, 0, Priority.SOMETIMES)
                .add(UniformMargins.defaultSpacerVertical(), 0, 1, Priority.NEVER)

                .add(generalOptions(), 0, 2, Priority.SOMETIMES)
                .add(UniformMargins.defaultSpacerVertical(), 0, 3, Priority.NEVER)

                .add(interfaceOptions(), 0, 4, Priority.SOMETIMES)
                .add(UniformMargins.defaultSpacerVertical(), 0, 5, Priority.NEVER)

                .add(debugOptions(), 0, 6, Priority.SOMETIMES)
            ;

            DynamicInsets.horizontalUniform(allOptions, OpenPriority::width, UniformMargins.DEFAULT_HORIZONTAL_INSET);

            return allOptions;
        }

        private static Uniform generalOptions()
        {
            HoverLabel generalLabel = HoverLabel.configure("label-options-general", Color.TEXT_0, Color.ACCENT_1, Weight.SEMIBOLD, Size.LARGE);

            ChoiceBox<String> localeSelect = new ChoiceBox<>();
            localeSelect.getItems().addAll(Locale.Variant.translationKeySet());
            localeSelect.setValue(Locale.get(Options.General.ACTIVE_LOCALE.get().translationKey()));
            Scale.scaleMinWidth(localeSelect, OpenPriority::width, Scale.MINOR.adjust(0.75));
            IStyle.apply(localeSelect, Size.REGULAR);

            Locale.bind(() ->
            {
                localeSelect.getItems().clear();
                localeSelect.getItems().addAll(Locale.Variant.translationKeySet());
                localeSelect.setValue(Locale.get(Options.General.ACTIVE_LOCALE.get().translationKey()));
            });

            Button applyLocale = ControlFactory.dynamicButton("action-apply-locale", OpenPriority::width, Scale.MINOR.adjust(0.5), () ->
            {
                Locale.Variant next = Options.General.ACTIVE_LOCALE.get().inverseRetrieve(localeSelect.getValue());

                if (next != Options.General.ACTIVE_LOCALE.get())
                {
                    Options.General.ACTIVE_LOCALE.set(next);
                    Locale.refreshIndices();
                }
            });

//            applyLocale.setMaxHeight(Double.MAX_VALUE);

            Label localeLabel = ControlFactory.label("label-language-select", Size.REGULAR, TEXT0);

            Uniform localeSelection = GridFactory.uniform(20, 3, 1)
                .add(localeLabel, 0, 0, Priority.NEVER)
                .add(localeSelect, 1, 0, Priority.SOMETIMES)
                .add(applyLocale, 2, 0, Priority.ALWAYS, Priority.ALWAYS);

            Uniform generalOptions = GridFactory.autoUniform(20, 1, 2, BG0)
                .add(generalLabel, 1, 1, Priority.ALWAYS)
                .add(localeSelection, 1, 2, Priority.ALWAYS);

            return generalOptions;
        }

        private static Uniform interfaceOptions()
        {
            HoverLabel interfaceLabel = HoverLabel.configure("label-options-interface", Color.TEXT_0, Color.ACCENT_1, Weight.SEMIBOLD, Size.LARGE);
            CheckBox showInformation = ControlFactory.optionCheckBox("option-show-information", Options.Interface.SHOW_INFORMATION, Display::applyInformationDisplaySetting);
            CheckBox showTime = ControlFactory.optionCheckBox("option-show-time", Options.Interface.SHOW_TIME, Display::applyTimeDisplaySetting);
            Button updateInterface = ControlFactory.button("action-update-interface", Double.MAX_VALUE, () ->
            {

            });

            Uniform interfaceOptions = GridFactory.autoUniform(20, 1, 3, IStyle.join(Color.UI_0, Part.BACKGROUND))
                .add(interfaceLabel, 1, 1, Priority.ALWAYS)
                .add(showInformation, 1, 2, Priority.ALWAYS)
                .add(showTime, 1, 3, Priority.ALWAYS);
//                .add(updateInterface, 1, 4, Priority.ALWAYS);

            return interfaceOptions;
        }

        private static Uniform debugOptions()
        {
            HoverLabel debugLabel = HoverLabel.configure("label-options-debug", Color.TEXT_0, Color.ACCENT_1, Weight.SEMIBOLD, Size.LARGE);

            CheckBox localeDebug = ControlFactory.checkBox("option-locale-debug");
            localeDebug.setSelected(Options.Debug.LOCALE_DEBUG.get());
            localeDebug.selectedProperty().addListener((obs, prev, next) ->
            {
                Options.Debug.LOCALE_DEBUG.set(next);
                Locale.refreshIndices();
            });

            Button updateCSS = ControlFactory.button("action-update-css", Double.MAX_VALUE, OpenPriority::updateStylesheets);
            Button updateLocale = ControlFactory.button("action-update-locale", Double.MAX_VALUE, Locale::refreshIndices);

            Uniform debugActions = GridFactory.uniform(10, 2, 1)
                .add(updateCSS, 0, 1, Priority.SOMETIMES)
                .add(updateLocale, 2, 1, Priority.SOMETIMES);

            Uniform debugOptions = GridFactory.autoUniform(20, 1, 3, BG0)
                .add(debugLabel, 1, 1, Priority.ALWAYS)
                .add(localeDebug, 1, 2, Priority.ALWAYS)
                .add(debugActions, 1, 3, Priority.ALWAYS);

            return debugOptions;
        }
    }
}
