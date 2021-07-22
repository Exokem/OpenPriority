package openpriority.panels.options;

import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import openpriority.OpenPriority;
import openpriority.api.Options;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.components.controls.HoverLabel;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.css.Size;
import openpriority.api.factories.ControlFactory;
import openpriority.api.factories.GridFactory;
import openpriority.api.responsive.DynamicRegion;
import openpriority.api.responsive.Locale;
import openpriority.api.responsive.Scale;
import openpriority.panels.Display;
import openpriority.panels.UniformMargins;

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
            DynamicRegion margin = UniformMargins.defaultMarginSide(Color.UI_0.join(IStyle.Part.BACKGROUND));

            Uniform root = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.NEVER, Priority.ALWAYS)
                .add(margin)
                .add(optionsContent(), Priority.ALWAYS)
                .add(margin.copy())
                .build(Color.UI_1.join(IStyle.Part.BACKGROUND));

            return root;
        }

        private static Uniform optionsContent()
        {
            Uniform optionsContent = GridFactory.AlignedUniformBuilder.start(Alignment.VERTICAL)
                .withSpacers(UniformMargins::defaultSpacerVertical)
                .defaultPriorities(Priority.SOMETIMES)
                .withPadding(20)
                .add(SECTION_TITLE_FACTORY.produce("section-options"), Priority.NEVER)
                .add(generalOptions())
                .add(interfaceOptions())
                .add(debugOptions())
                .build(IStyle.custom("border-lr"));

            return optionsContent;
        }

        private static Uniform generalOptions()
        {
            ChoiceBox<String> localeSelect = new ChoiceBox<>();
            localeSelect.getItems().addAll(Locale.Variant.translationKeySet());
            localeSelect.setValue(Locale.get(Options.General.ACTIVE_LOCALE.get().translationKey()));
            localeSelect.setMaxWidth(Double.MAX_VALUE);
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
                .add(localeSelect, Priority.ALWAYS)
                .build();

            Uniform localeHolder = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .withGap(20)
                .add(localeSelection)
                .add(applyLocale)
                .distributeSpaceEvenly()
                .build();

            GridPane.setHalignment(applyLocale, HPos.RIGHT);

            Uniform generalOptions = MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(ControlFactory.HEADING_FACTORY.produce("label-options-general"), Priority.ALWAYS)
                .add(localeHolder)
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

            Uniform buttons = GridFactory.AlignedUniformBuilder.start(Alignment.HORIZONTAL)
                .defaultPriorities(Priority.SOMETIMES)
                .withGap(20)
                .addAll(updateCSS, updateLocale)
                .distributeSpaceEvenly()
                .build();

            Uniform debugOptions = MENU_SECTION_BUILDER
                .defaultPriorities(Priority.ALWAYS)
                .add(debugLabel)
                .add(localeDebug)
                .add(buttons)
                .build();

            return debugOptions;
        }
    }
}
