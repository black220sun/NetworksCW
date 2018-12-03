package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BFAlgorithmFactory implements PathFindingAlgorithmFactory {
    @Override
    public PathFindingAlgorithm getAlgorithm(@NotNull List<GraphNode> nodes,
                                             @NotNull List<Channel> links) {
        return new BellmanFordAlgorithm(nodes, links);
    }
}
