package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

public interface ChannelFactory {
    default Channel createChannel(@NotNull GraphNode from, @NotNull GraphNode to, int weight) {
        return createChannel(from, to, weight, Channel.ERRORS_AMOUNT);
    }

    Channel createChannel(@NotNull GraphNode from, @NotNull GraphNode to, int weight, double errors);
}
