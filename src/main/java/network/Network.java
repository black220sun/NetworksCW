package network;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Label;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.model.MutableNode;
import graph.algorithm.DejkstraFactory;
import graph.algorithm.GraphPath;
import graph.algorithm.Algorithm;
import graph.algorithm.Factory;
import graph.edge.Edge;
import graph.vertex.Vertex;
import utils.Stringable;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static guru.nidi.graphviz.model.Factory.*;

public class Network implements Stringable {
    private List<Vertex> nodes;
    private final Topology topology;
    private Factory factory;
    private List<GraphPath> openConnections;

    public Network() {
        this(new Topology() {
            @Override
            public List<Vertex> createNetwork() {
                return new ArrayList<>();
            }

            @Override
            public Vertex createNode() {
                return null;
            }
        });
    }

    public Network(@NotNull Topology topology) {
        this(topology, new DejkstraFactory());
    }

    public Network(@NotNull Topology topology, Factory factory) {
        this.topology = topology;
        this.factory = factory;
        generateNetwork();
    }

    public void generateNetwork() {
        nodes = topology.createNetwork();
        openConnections = new ArrayList<>();
    }

    public List<Vertex> getNodes() {
        return nodes;
    }

    public List<Vertex> getNodes(boolean terminal) {
        if (!terminal)
            return nodes;
        return nodes.stream()
                .filter(Vertex::isTerminal)
                .collect(Collectors.toList());
    }

    public Vertex getRandomNode() {
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    public Vertex getRandomNode(boolean terminal) {
        List<Vertex> nodes = getNodes(terminal);
        return nodes.get(new Random().nextInt(nodes.size()));
    }

    public Factory getFactory() {
        return factory;
    }

    public void setFactory(Factory factory) {
        this.factory = factory;
    }

    public GraphPath getPath(@NotNull Vertex from, @NotNull Vertex to) {
        return getPath(from, to, false);
    }

    public GraphPath getPath(@NotNull Vertex from, @NotNull Vertex to, boolean isUsed) {
        if (!exists(from, to))
            return new GraphPath();
        return factory.getAlgorithm(nodes, getChannels(isUsed)).getPath(from, to);
    }

    public List<GraphPath> getPaths(@NotNull Vertex from) {
        Algorithm algorithm = factory.getAlgorithm(nodes, getChannels());
        return nodes.stream()
                .filter(node -> !node.equals(from))
                .map(node -> algorithm.getPath(from, node))
                .collect(Collectors.toList());
    }

    private List<Edge> getChannels() {
        return nodes.stream()
                .flatMap(node -> node.getConnections().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Edge> getChannels(boolean isUsed) {
        return nodes.stream()
                .flatMap(node -> node.getConnections().stream())
                .distinct()
                .filter(ch -> ch.isUsed() == isUsed)
                .collect(Collectors.toList());
    }

    public GraphPath createConnection(@NotNull Vertex from, @NotNull Vertex to) {
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


    public void closeConnection(@NotNull Vertex from, @NotNull Vertex to) {
        openConnections.stream()
                .filter(path -> path.ofNodes(from, to))
                .findAny()
                .ifPresent(this::closeConnection);
    }

    public Vertex addNode() {
        Vertex node = topology.createNode();
        if (node != null) {
            nodes.add(node);
        }
        return node;
    }

    public void addNode(@NotNull Vertex node) {
        if (!exists(node)) {
            nodes.add(node);
        }
    }

    public void removeNode(@NotNull Vertex node) {
        Edge[] connections = node.getConnections().toArray(new Edge[]{});
        for (Edge ch: connections) {
            ch.remove();
        }
        nodes.remove(node);
    }

    public void addConnection(@NotNull Vertex from, @NotNull Vertex to, int weight) {
        if (exists(from, to)) {
            from.addConnectedNode(to, weight);
        }
    }

    public void removeConnection(@NotNull Vertex from, @NotNull Vertex to) {
        from.removeConnectedNode(to);

    }

    private boolean exists(Vertex... nodes) {
        return Arrays.stream(nodes)
                .allMatch(node -> this.nodes.contains(node) && node.isTerminal());
    }

    // TODO(cache?)
    double getAvgOrder() {
        if (nodes.isEmpty())
            return 0;
        double sum = 0;
        for (Vertex node : nodes) {
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
        PropertiesHandler cfg = PropertiesHandler.getProps();
        HashMap<Vertex, MutableNode> map = new HashMap<>();
        MutableGraph g = mutGraph("network")
                .setDirected(true);
        nodes.forEach(node -> {
            Color color = cfg.getColor("vertex");
            if (node.isTerminal())
                color = cfg.getColor("terminal");
            if (node.isSelected())
                color = cfg.getColor("selectedN");
            map.put(node, mutNode(node.toString()).add(color).add(Style.FILLED));
        });
        nodes.forEach(node -> {
            MutableNode mNode = map.get(node);
            g.add(mNode);
            node.getConnections().forEach(ch -> {
                MutableNode toNode = map.get(ch.getToNode());
                Color color = cfg.getColor("edge");
                if (ch.isUsed())
                    color = cfg.getColor("connected");
                if (ch.isSelected())
                    color = cfg.getColor("selectedC");
                mNode.addLink(to(toNode).
                        with(Label.markdown(String.valueOf(ch.getWeight())),
                                Style.DASHED,
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
