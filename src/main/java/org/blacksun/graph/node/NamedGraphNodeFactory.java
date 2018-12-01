package org.blacksun.graph.node;

import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.graph.channel.DuplexChannelFactory;
import org.jetbrains.annotations.NotNull;

public class NamedGraphNodeFactory implements GraphNodeFactory {
    private final String name;
    private final ChannelFactory factory;
    private int nodesCounter;

    public NamedGraphNodeFactory() {
        this("Node");
    }

    public NamedGraphNodeFactory(@NotNull String name) {
        this(name, new DuplexChannelFactory());
    }

    public NamedGraphNodeFactory(@NotNull String name, @NotNull ChannelFactory factory) {
        this.name = name;
        this.factory = factory;
        nodesCounter = 0;
    }

    @Override
    public GraphNode createNode() {
        return new NamedGraphNode(name + nodesCounter++, factory);
    }
}
