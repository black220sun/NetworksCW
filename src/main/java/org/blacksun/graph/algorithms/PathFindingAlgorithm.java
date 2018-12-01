package org.blacksun.graph.algorithms;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface PathFindingAlgorithm {
    Integer INFINITY = Integer.MAX_VALUE;

    boolean isAccessible(@NotNull GraphNode from, @NotNull GraphNode to);
    int getMinDistance(@NotNull GraphNode from, @NotNull GraphNode to);
    HashMap<GraphNode, Integer> getDistances(@NotNull GraphNode node);
    GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to);
}
