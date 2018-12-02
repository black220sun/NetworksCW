package org.blacksun.graph.node;

import org.blacksun.graph.channel.ChannelFactory;
import org.jetbrains.annotations.NotNull;

public class SimpleNode extends NamedGraphNode {
    SimpleNode(@NotNull String name, @NotNull ChannelFactory factory) {
        super(name, factory);
    }

    @Override
    public String toString() {
        return getName();
    }
}
