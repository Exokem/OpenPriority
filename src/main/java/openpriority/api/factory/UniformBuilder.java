package openpriority.api.factory;

import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import openpriority.api.annotation.FactoryOperation;
import openpriority.api.component.layout.Alignment;
import openpriority.api.component.layout.LinkedUniform;
import openpriority.api.component.layout.Uniform;
import openpriority.api.css.Color;
import openpriority.api.css.IStyle;
import openpriority.api.responsive.DynamicRegion;
import openpriority.panel.UniformMargins;
import openpriority.task.Task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.function.Supplier;

import static openpriority.api.annotation.OperationStage.*;

public class UniformBuilder<V extends Uniform>
{
    public static final UniformBuilder<Uniform> MENU_SECTION_BUILDER = bind(Uniform::new, Alignment.VERTICAL)
        .withPadding(20).withSpacers(UniformMargins::defaultSpacerVertical)
        .withStyles(Color.UI_0.join(IStyle.Part.BACKGROUND));

    public static final UniformBuilder<LinkedUniform<Uniform, Task>>
        TASK_LIST_BUILDER = new UniformBuilder<>(() -> new LinkedUniform<>(Alignment.VERTICAL), Alignment.VERTICAL);

    public static <V extends Uniform> UniformBuilder<V> bind(Supplier<V> constructor, Alignment axis)
    {
        return new UniformBuilder<>(constructor, axis);
    }

    protected UniformBuilder(Supplier<V> constructor, Alignment axis)
    {
        this.constructor = constructor;
        this.axis = axis;
    }

    private final Supplier<V> constructor;
    private final Alignment axis;

    private double padding = 0;
    private double hGap, vGap;
    private Uniform uniform;

    private boolean skipFirst = true;
    private Supplier<DynamicRegion> spacers = null;
    private Priority[] defaultPriorities = null;

    private final LinkedHashMap<Node, Priority[]> content = new LinkedHashMap<>();
    private IStyle[] buildStyles = {};

    private final Set<ColumnConstraints> columnConstraintsSet = new HashSet<>();
    private final Set<RowConstraints> rowConstraintsSet = new HashSet<>();

    private void addInternal(Node child, Priority... priorities)
    {
        content.put(child, priorities);
    }

    @FactoryOperation
    public UniformBuilder<V> add(Node child, Priority... priorities)
    {
        if (child == null) return this;

        if (priorities.length == 0 && defaultPriorities != null) priorities = defaultPriorities;

        if (spacers != null && (content.size() != 0 || !skipFirst)) addInternal(spacers.get());
        addInternal(child, priorities);
        return this;
    }

    @FactoryOperation
    public UniformBuilder<V> addAll(Node... nodes)
    {
        for (Node node : nodes)
        {
            if (defaultPriorities != null) add(node, defaultPriorities);
            else add(node);
        }

        return this;
    }

    @FactoryOperation
    public UniformBuilder<V> withPadding(double padding)
    {
        this.padding = padding;
        return this;
    }

    @FactoryOperation(stage = PREADD)
    public UniformBuilder<V> withSpacers(Supplier<DynamicRegion> spacers)
    {
        this.spacers = spacers;
        return this;
    }

    @FactoryOperation(stage = PREADD)
    public UniformBuilder<V> withSpacers(Supplier<DynamicRegion> spacers, boolean skipFirst)
    {
        this.spacers = spacers;
        this.skipFirst = skipFirst;
        return this;
    }

    @FactoryOperation
    public UniformBuilder<V> withGap(double gap)
    {
        this.hGap = this.vGap = gap;
        return this;
    }

    @FactoryOperation
    public UniformBuilder<V> withGap(double hGap, double vGap)
    {
        this.hGap = hGap;
        this.vGap = vGap;
        return this;
    }

    @FactoryOperation(stage = PREADD)
    public UniformBuilder<V> defaultPriorities(Priority... priorities)
    {
        if (priorities.length != 0) this.defaultPriorities = priorities;

        return this;
    }

    @FactoryOperation
    public UniformBuilder<V> withStyles(IStyle... buildStyles)
    {
        this.buildStyles = buildStyles;
        return this;
    }

    @FactoryOperation(stage = POSTADD)
    public UniformBuilder<V> distributeSpaceEvenly()
    {
        double percent = 100.0D / (content.size() - (skipFirst && spacers != null ? 1 : 0));

        switch (axis)
        {
            case HORIZONTAL -> {
                ColumnConstraints constraints = new ColumnConstraints();
                constraints.setPercentWidth(percent);
                columnConstraintsSet.add(constraints);
            }
            case VERTICAL -> {
                RowConstraints constraints = new RowConstraints();
                constraints.setPercentHeight(percent);
                rowConstraintsSet.add(constraints);
            }
        }

        return this;
    }

    @FactoryOperation(stage = POSTADD)
    public UniformBuilder<V> withColumnConstraints(ColumnConstraints... constraints)
    {
        columnConstraintsSet.addAll(Arrays.asList(constraints));
        return this;
    }

    @FactoryOperation(stage = POSTADD)
    public UniformBuilder<V> withRowConstraints(RowConstraints... constraints)
    {
        rowConstraintsSet.addAll(Arrays.asList(constraints));
        return this;
    }

    protected void buildAdd(V uniform, Node node, int column, int row, Priority[] priorities)
    {
        uniform.add(node, column, row, priorities);
    }

    @FactoryOperation(stage = BUILD)
    public V build(IStyle... styles)
    {
        V uniform = constructor.get();

        uniform.setHgap(hGap);
        uniform.setVgap(vGap);

        if (padding != 0) uniform.inset(padding);

        int index = 0;

        for (Node node : content.keySet())
        {
            buildAdd(uniform, node, axis.column(index), axis.row(index), content.get(node));
            index++;
        }

        uniform.addColumnConstraints(columnConstraintsSet);
        columnConstraintsSet.clear();

        uniform.addRowConstraints(rowConstraintsSet);
        rowConstraintsSet.clear();

        content.clear();

        uniform.align(axis);

        return IStyle.apply(IStyle.apply(uniform, styles), buildStyles);
    }

    @FactoryOperation(stage = BUILD)
    public V buildWithConstraints(ColumnConstraints columnConstraints, RowConstraints rowConstraints, IStyle... styles)
    {
        V uniform = build(styles);
        uniform.addColumnConstraints(columnConstraints);
        uniform.addRowConstraints(rowConstraints);
        return uniform;
    }
}
