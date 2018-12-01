package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public class BFAlgorithmFactory implements PathFindingAlgorithmFactory {
    @Override
    public PathFindingAlgorithm getAlgorithm(@NotNull List<GraphNode> nodes) {
        List<Channel> channels = nodes.stream()
                .flatMap(node -> node.getConnections().stream())
                .distinct()
                .filter(ch -> !ch.isUsed())
                .collect(Collectors.toList());
        return new BellmanFordAlgorithm(nodes, channels);
    }
}
