package org.blacksun.graph.node;

import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.utils.Config;
import org.jetbrains.annotations.NotNull;

public class NamedGraphNodeFactory implements GraphNodeFactory {
    protected final String name;
    protected final ChannelFactory factory;
    protected int nodesCounter;

    public NamedGraphNodeFactory() {
        this("Node");
    }

    public NamedGraphNodeFactory(@NotNull String name) {
        this(name, Config.getConfig().getProperty("channelFactory"));
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
