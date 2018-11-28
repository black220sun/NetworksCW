package org.blacksun.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Network {
    private List<GraphNode> nodes;

    public Network(@NotNull Topology topology) {
        nodes = topology.createNetwork();
    }

    // TODO(cache?)
    private double getAvgOrder() {
        if (nodes.isEmpty())
            return 0;
        double sum = 0;
        for (GraphNode node : nodes) {
            sum += node.getOrder();
        }
        return sum / nodes.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Network[order=")
                .append(getAvgOrder())
                .append("] {\n");
        nodes.forEach(node -> sb.append(node.toFullString()).append(",\n"));
        sb.append("}");
        return sb.toString();
    }
}
