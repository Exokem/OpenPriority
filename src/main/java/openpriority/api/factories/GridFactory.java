package openpriority.api.factories;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Rectangle;
import openpriority.api.components.Alignment;
import openpriority.api.components.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.DynamicRegion;
import openpriority.api.responsive.DynamicResizable;
import openpriority.panels.UniformMargins;

import java.util.LinkedHashMap;
import java.util.function.Supplier;

public final class GridFactory
{
    public static Uniform uniform(double gap, int columns, int rows, IStyle... styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        IStyle.apply(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, IStyle... styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }

    public static Uniform uniform(double gap, int columns, int rows, String styles)
    {
        Uniform uniform = new Uniform(columns, rows);

        uniform.setHgap(gap);
        uniform.setVgap(gap);

        IStyle.apply(uniform, styles);

        return uniform;
    }

    public static Uniform autoUniform(double gap, int innerColumns, int innerRows, String styles)
    {
        return uniform(gap, innerColumns + 2, innerRows + 2, styles);
    }

    public static Uniform uniformButtonBar(double gap, Node... buttons)
    {
        Uniform container = AlignedUniformBuilder.start(Alignment.HORIZONTAL)
            .withGap(gap)
            .defaultPriorities(Priority.SOMETIMES)
            .addAll(buttons)
            .build();

        ColumnConstraints constraints = new ColumnConstraints();
        constraints.setPercentWidth(100.0D / (double) buttons.length);
        container.getColumnConstraints().add(constraints);

        return container;
    }

    public static Rectangle paddingRect(Supplier<Double> widthBasis, double widthFactor, Supplier<Double> heightBasis, double heightFactor)
    {
        Rectangle rect = new Rectangle(widthBasis.get(), heightBasis.get());
        rect.setVisible(false);

        DynamicResizable.addListener(() ->
        {
            rect.setWidth(widthBasis.get() * widthFactor);
            rect.setHeight(heightBasis.get() * heightFactor);
        });

        return rect;
    }

    public static final AlignedUniformBuilder MENU_SECTION_BUILDER = AlignedUniformBuilder.start(Alignment.VERTICAL)
        .withPadding(20).withSpacers(UniformMargins::defaultSpacerVertical)
        .withStyles(Color.UI_0.join(IStyle.Part.BACKGROUND));

    public static class AlignedUniformBuilder
    {
        public static AlignedUniformBuilder start(Alignment axis)
        {
            return new AlignedUniformBuilder(axis);
        }

        private AlignedUniformBuilder(Alignment axis)
        {
            this.axis = axis;
        }

        private final Alignment axis;
        private double padding = 0;
        private double hGap, vGap;
        private Uniform uniform;

        private boolean skipFirst = true;
        private Supplier<DynamicRegion> spacers = null;
        private Priority[] defaultPriorities = null;

        private final LinkedHashMap<Node, Priority[]> content = new LinkedHashMap<>();
        private IStyle[] buildStyles = {};

        private void addInternal(Node child, Priority... priorities)
        {
            content.put(child, priorities);
        }

        public AlignedUniformBuilder add(Node child, Priority... priorities)
        {
            if (priorities.length == 0 && defaultPriorities != null) priorities = defaultPriorities;

            if (spacers != null && (content.size() != 0 || !skipFirst)) addInternal(spacers.get());
            addInternal(child, priorities);
            return this;
        }

        public AlignedUniformBuilder addAll(Node... nodes)
        {
            for (Node node : nodes)
            {
                add(node, defaultPriorities);
            }

            return this;
        }

        public AlignedUniformBuilder withPadding(double padding)
        {
            this.padding = padding;
            return this;
        }

        public AlignedUniformBuilder withSpacers(Supplier<DynamicRegion> spacers)
        {
            this.spacers = spacers;
            return this;
        }

        public AlignedUniformBuilder withSpacers(Supplier<DynamicRegion> spacers, boolean skipFirst)
        {
            this.spacers = spacers;
            this.skipFirst = skipFirst;
            return this;
        }

        public AlignedUniformBuilder withGap(double gap)
        {
            this.hGap = this.vGap = gap;
            return this;
        }

        public AlignedUniformBuilder withGap(double hGap, double vGap)
        {
            this.hGap = hGap;
            this.vGap = vGap;
            return this;
        }

        public AlignedUniformBuilder defaultPriorities(Priority... priorities)
        {
            if (priorities.length != 0) this.defaultPriorities = priorities;

            return this;
        }

        public AlignedUniformBuilder withStyles(IStyle... buildStyles)
        {
            this.buildStyles = buildStyles;
            return this;
        }

        public Uniform build(IStyle... styles)
        {
            Uniform uniform = new Uniform();

            uniform.setHgap(hGap);
            uniform.setVgap(vGap);

            if (padding != 0) uniform.inset(padding);

            int index = 0;

            for (Node node : content.keySet())
            {
                uniform.add(node, axis.column(index), axis.row(index), content.get(node));
                index ++;
            }

            content.clear();

            return IStyle.apply(IStyle.apply(uniform, styles), buildStyles);
        }
    }
}
