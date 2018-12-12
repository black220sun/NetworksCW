package graph.vertex;

import graph.edge.ChannelFactory;
import org.jetbrains.annotations.NotNull;

public class Node extends NamedVertex {
    Node(@NotNull String name, @NotNull ChannelFactory factory) {
        super(name, factory);
    }

    @Override
    public String toString() {
        return getName();
    }
}
