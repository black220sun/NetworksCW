package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

public class SimplexChannelFactory implements ChannelFactory {
    @Override
    public Channel createChannel(@NotNull GraphNode from, @NotNull GraphNode to, int weight, double errors) {
        return new SimplexChannel(from, to, weight, errors);
    }
}
