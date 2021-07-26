package openpriority.api.components.layouts;

import javafx.scene.Node;

public interface ILinkedUniformDisplayable<X extends Node>
{
    X display();
    String key();
}
