package org.blacksun.graph.node;

import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.utils.Config;
import org.jetbrains.annotations.NotNull;

public class SimpleNodeFactory extends NamedGraphNodeFactory {
    private final Config cfg = Config.getConfig();

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
        int counter = cfg.getInt("counter");
        GraphNode node = new SimpleNode(name + counter, cfg.getProperty("channelFactory"));
        cfg.setProperty("counter", ++counter);
        return node;
    }
}
