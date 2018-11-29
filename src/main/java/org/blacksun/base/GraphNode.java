package org.blacksun.base;

import org.blacksun.utils.Pair;
import org.blacksun.utils.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GraphNode extends StringRepresentable {
    List<GraphNode> getConnectedNodes();
    // returns list or connected nodes with the weight of connection
    List<Pair<GraphNode, Integer>> getConnections();
    boolean isConnected(@NotNull GraphNode node);
    void addConnectedNode(@NotNull GraphNode node, int weight);
    void addConnection(@NotNull Channel channel);
    void removeConnectedNode(@NotNull GraphNode node);
    void removeConnection(@NotNull Channel channel);
    int getOrder();
}
