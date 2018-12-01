package org.blacksun.network;

import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.algorithms.PathFindingAlgorithm;
import org.blacksun.graph.algorithms.PathFindingAlgorithmFactory;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Network implements StringRepresentable {
    private List<GraphNode> nodes;
    private PathFindingAlgorithmFactory factory;

    public Network(@NotNull Topology topology) {
        this(topology, null);
    }

    public Network(@NotNull Topology topology, PathFindingAlgorithmFactory factory) {
        nodes = topology.createNetwork();
        this.factory = factory;
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public GraphNode getRandomNode() {
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    public PathFindingAlgorithmFactory getFactory() {
        return factory;
    }

    public void setFactory(PathFindingAlgorithmFactory factory) {
        this.factory = factory;
    }

    public GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (!nodes.contains(from) || ! nodes.contains(to))
            return new GraphPath();
        return factory.getAlgorithm(nodes).getPath(from, to);
    }

    public GraphPath createConnection(@NotNull GraphNode from, @NotNull GraphNode to) {
        GraphPath path = getPath(from, to);
        if (path.exists()) {
            path.forEach(ch -> ch.setUsed(true));
        }
        return path;
    }

    public void closeConnection(@NotNull GraphPath path) {
        path.forEach(ch -> ch.setUsed(false));
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
