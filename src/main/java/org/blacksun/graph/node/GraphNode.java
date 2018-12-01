package org.blacksun.graph.node;

import org.blacksun.graph.channel.Channel;
import org.blacksun.utils.Pair;
import org.blacksun.utils.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GraphNode extends StringRepresentable {
    List<GraphNode> getConnectedNodes();
    // returns list or connected nodes with the weight of connection
    List<Pair<GraphNode, Integer>> getConnections();
    boolean isConnected(@NotNull GraphNode node);
    Channel getConnection(@NotNull GraphNode node);
    Channel addConnectedNode(@NotNull GraphNode node, int weight);
    Channel addConnection(@NotNull Channel channel);
    void removeConnectedNode(@NotNull GraphNode node);
    void removeConnection(@NotNull Channel channel);
    int getOrder();
}
