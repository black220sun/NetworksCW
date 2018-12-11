package org.blacksun.network;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import org.blacksun.graph.algorithms.BFAlgorithmFactory;
import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.algorithms.PathFindingAlgorithm;
import org.blacksun.graph.algorithms.PathFindingAlgorithmFactory;
import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.StringRepresentable;
import org.blacksun.utils.Config;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.*;

public class Network implements StringRepresentable {
    private List<GraphNode> nodes;
    private final Topology topology;
    private PathFindingAlgorithmFactory factory;
    private List<GraphPath> openConnections;

    public Network() {
        this(new Topology() {
            @Override
            public List<GraphNode> createNetwork() {
                return new ArrayList<>();
            }

            @Override
            public GraphNode createNode() {
                return null;
            }
        });
    }

    public Network(@NotNull Topology topology) {
        this(topology, new BFAlgorithmFactory());
    }

    public Network(@NotNull Topology topology, PathFindingAlgorithmFactory factory) {
        this.topology = topology;
        this.factory = factory;
        generateNetwork();
    }

    public void generateNetwork() {
        nodes = topology.createNetwork();
        openConnections = new ArrayList<>();
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

    public List<GraphNode> getNodes(boolean terminal) {
        if (!terminal)
            return nodes;
        return nodes.stream()
                .filter(GraphNode::isTerminal)
                .collect(Collectors.toList());
    }

    public GraphNode getRandomNode() {
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    public GraphNode getRandomNode(boolean terminal) {
        List<GraphNode> nodes = getNodes(terminal);
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    public PathFindingAlgorithmFactory getFactory() {
        return factory;
    }

    public void setFactory(PathFindingAlgorithmFactory factory) {
        this.factory = factory;
    }

    public GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to) {
        return getPath(from, to, false);
    }

    public GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to, boolean isUsed) {
        if (!exists(from, to))
            return new GraphPath();
        return factory.getAlgorithm(nodes, getChannels(isUsed)).getPath(from, to);
    }

    public List<GraphPath> getPaths(@NotNull GraphNode from) {
        PathFindingAlgorithm algorithm = factory.getAlgorithm(nodes, getChannels());
        return nodes.stream()
                .filter(node -> !node.equals(from))
                .map(node -> algorithm.getPath(from, node))
                .collect(Collectors.toList());
    }

    private List<Channel> getChannels() {
        return nodes.stream()
                .flatMap(node -> node.getConnections().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Channel> getChannels(boolean isUsed) {
        return nodes.stream()
                .flatMap(node -> node.getConnections().stream())
                .distinct()
                .filter(ch -> ch.isUsed() == isUsed)
                .collect(Collectors.toList());
    }

    public GraphPath createConnection(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (from == to)
            return new GraphPath();
        GraphPath path = getPath(from, to);
        if (path.exists()) {
            path.forEach(ch -> ch.setUsed(true));
            openConnections.add(path);
        }
        return path;
    }

    public void closeConnection(@NotNull GraphPath path) {
        if (openConnections.contains(path)) {
            path.forEach(ch -> ch.setUsed(false));
            openConnections.remove(path);
        }
    }


    public void closeConnection(@NotNull GraphNode from, @NotNull GraphNode to) {
        openConnections.stream()
                .filter(path -> path.ofNodes(from, to))
                .findAny()
                .ifPresent(this::closeConnection);
    }

    public GraphNode addNode() {
        GraphNode node = topology.createNode();
        if (node != null) {
            nodes.add(node);
        }
        return node;
    }

    public void addNode(@NotNull GraphNode node) {
        if (!exists(node)) {
            nodes.add(node);
        }
    }

    public void removeNode(@NotNull GraphNode node) {
        Channel[] connections = node.getConnections().toArray(new Channel[]{});
        for (Channel ch: connections) {
            ch.remove();
        }
        nodes.remove(node);
    }

    public void addConnection(@NotNull GraphNode from, @NotNull GraphNode to, int weight) {
        if (exists(from, to)) {
            from.addConnectedNode(to, weight);
        }
    }

    public void removeConnection(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (exists(from, to)) {
            from.removeConnectedNode(to);
        }
    }

    private boolean exists(GraphNode... nodes) {
        return Arrays.stream(nodes)
                .allMatch(node -> this.nodes.contains(node));
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

    public MutableGraph toGraph() {
        Config cfg = Config.getConfig();
        HashMap<GraphNode, MutableNode> map = new HashMap<>();
        MutableGraph g = mutGraph("network")
                .setDirected(true);
        nodes.forEach(node -> {
            Color color = cfg.getColor("node");
            if (node.isTerminal())
                color = cfg.getColor("terminal");
            if (node.isSelected())
                color = cfg.getColor("selectedN");
            map.put(node, mutNode(node.toString()).add(color));
        });
        nodes.forEach(node -> {
            MutableNode mNode = map.get(node);
            g.add(mNode);
            node.getConnections().forEach(ch -> {
                MutableNode toNode = map.get(ch.getToNode());
                Color color = cfg.getColor("channel");
                if (ch.isUsed())
                    color = cfg.getColor("connected");
                if (ch.isSelected())
                    color = cfg.getColor("selectedC");
                mNode.addLink(to(toNode).
                        with(Label.markdown(String.valueOf(ch.getWeight())),
                                color));
            });
        });
        return g;
    }

    @Override
    public String toString() {
        return "Network[nodes=" + nodes.size() + ", order=" + getAvgOrder() + "]";
    }

    public void closeAll() {
        getChannels(true).forEach(ch -> ch.setUsed(false));
    }

    public void clear() {
        nodes.clear();
        openConnections.clear();
    }
}
