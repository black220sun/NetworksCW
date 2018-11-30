package org.blacksun.network;

import org.blacksun.graph.GraphNode;
import org.blacksun.utils.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Network implements StringRepresentable {
    private List<GraphNode> nodes;

    public Network(@NotNull Topology topology) {
        nodes = topology.createNetwork();
    }

    // TODO(cache?)
    double getAvgOrder() {
        if (nodes.isEmpty())
            return 0;
        double sum = 0;
        for (GraphNode node : nodes) {
            sum += node.getOrder();
        }
        return sum / nodes.size();
    }

    @Override
    public String stringRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append("Network[order=")
                .append(getAvgOrder())
                .append("] {\n");
        nodes.forEach(node -> sb.append(node.stringRepresentation()).append(",\n"));
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Network[nodes=" + nodes.size() + ", order=" + getAvgOrder() + "]";
    }
}
