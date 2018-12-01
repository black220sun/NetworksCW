package org.blacksun.graph.algorithms;

import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface PathFindingAlgorithmFactory {
    PathFindingAlgorithm getAlgorithm(@NotNull List<GraphNode> nodes);
}
