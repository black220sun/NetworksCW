package org.blacksun.graph.node;

import org.blacksun.graph.channel.ChannelFactory;
import org.jetbrains.annotations.NotNull;

public class SimpleNodeFactory extends NamedGraphNodeFactory {
    public SimpleNodeFactory() {
        super();
    }

    public SimpleNodeFactory(@NotNull String name) {
        super(name);
    }

    public SimpleNodeFactory(@NotNull String name, @NotNull ChannelFactory factory) {
        super(name, factory);
    }

    @Override
    public GraphNode createNode() {
        return new SimpleNode(name + nodesCounter++, factory);
    }
}
