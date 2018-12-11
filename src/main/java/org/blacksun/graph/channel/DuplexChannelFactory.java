package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

public class DuplexChannelFactory implements ChannelFactory {
    @Override
    public Channel createChannel(@NotNull GraphNode from, @NotNull GraphNode to, int weight, double errors) {
        return new DuplexChannel(from, to, weight, errors);
    }

    @Override
    public String toString() {
        return "Duplex";
    }
}
