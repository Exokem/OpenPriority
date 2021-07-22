package openpriority.panels.options;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;
import openpriority.panels.Display;
import openpriority.panels.UniformMargins;

import static openpriority.api.css.Style.BG1;
import static openpriority.api.css.Style.TEXT0;
import static openpriority.api.factories.ControlFactory.SECTION_TITLE_FACTORY;
import static openpriority.api.factories.GridFactory.MENU_SECTION_BUILDER;

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

                .add(allOptions(), 1, 1, Priority.ALWAYS, Priority.ALWAYS)
                ;

            return root;
        }

        private static Uniform allOptions()
        {
            Uniform allOptions = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .withSpacers(UniformMargins::defaultSpacerVertical)
                .withPadding(20)
                .defaultPriorities(Priority.SOMETIMES)
                .add(SECTION_TITLE_FACTORY.produce("section-options"))
                .add(generalOptions())
                .add(interfaceOptions())
                .add(debugOptions())
                .build();

            return allOptions;
        }

        private static Uniform generalOptions()
        {
            HoverLabel generalLabel = ControlFactory.HEADING_FACTORY.produce("label-options-general");

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

            Label localeLabel = ControlFactory.label("label-language-select", Size.REGULAR, TEXT0);

            Uniform localeSelection = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(20)
                .add(localeLabel, Priority.NEVER)
                .add(localeSelect, Priority.SOMETIMES)
                .add(applyLocale, Priority.ALWAYS, Priority.ALWAYS)
                .build();

            Uniform generalOptions = MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(generalLabel, Priority.ALWAYS)
                .add(localeSelection)
                .build();

            return generalOptions;
        }

        private static Uniform interfaceOptions()
        {
            HoverLabel interfaceLabel = ControlFactory.HEADING_FACTORY.produce("label-options-interface");
            CheckBox showInformation = ControlFactory.optionCheckBox("option-show-information", Options.Interface.SHOW_INFORMATION, Display::applyInformationDisplaySetting);
            CheckBox showTime = ControlFactory.optionCheckBox("option-show-time", Options.Interface.SHOW_TIME, Display::applyTimeDisplaySetting);

            Uniform interfaceOptions = MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(interfaceLabel)
                .add(showInformation)
                .add(showTime)
                .build();

            return interfaceOptions;
        }

        private static Uniform debugOptions()
        {
            HoverLabel debugLabel = ControlFactory.HEADING_FACTORY.produce("label-options-debug");

            CheckBox localeDebug = ControlFactory.checkBox("option-locale-debug");
            localeDebug.setSelected(Options.Debug.LOCALE_DEBUG.get());
            localeDebug.selectedProperty().addListener((obs, prev, next) ->
            {
                Options.Debug.LOCALE_DEBUG.set(next);
                Locale.refreshIndices();
            });

            Button updateCSS = ControlFactory.button("action-update-css", Double.MAX_VALUE, OpenPriority::updateStylesheets);
            Button updateLocale = ControlFactory.button("action-update-locale", Double.MAX_VALUE, Locale::refreshIndices);

            Uniform debugOptions = MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(debugLabel)
                .add(localeDebug)
                .add(GridFactory.uniformButtonBar(20, updateCSS, updateLocale))
                .build();

            return debugOptions;
        }
    }
}
