package openpriority.api.component.layout;

import javafx.scene.Node;

public interface ILinkedUniformDisplayable<X extends Node>
{
    X display();

    default String key()
    {
        return toString();
    }

    default void setSelected(boolean selected)
    {

    }
}
