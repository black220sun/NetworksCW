package org.blacksun.base;

import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface GraphNode {
    List<GraphNode> getConnectedNodes();
    // returns list or connected nodes with the weight of connection
    List<Pair<GraphNode, Integer>> getConnections();
    default boolean isConnected(@NotNull GraphNode node) {
        return isConnected(node, 1);
    }
    // returns true if current node is connected to `node` with no more than `distance` links
    // TODO(distance in links or in weight?)
    // TODO(method for distance in weight?)
    boolean isConnected(@NotNull GraphNode node, int distance);
    void addConnectedNode(@NotNull GraphNode node, int weight);
    void removeConnectedNode(@NotNull GraphNode node);

    int getOrder();

    String toFullString();
}
