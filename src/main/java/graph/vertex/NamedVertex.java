package graph.vertex;

import graph.edge.Edge;
import graph.edge.ChannelFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NamedVertex implements Vertex {
    private String name;
    private final ChannelFactory factory;
    private List<Edge> connections;
    private boolean selected;
    private boolean terminal;

    NamedVertex(@NotNull String name, @NotNull ChannelFactory factory) {
        this.name = name;
        this.factory = factory;
        terminal = false;
        connections = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public List<Vertex> getConnectedNodes() {
        return connections.stream()
                .map(Edge::getToNode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Edge> getConnections() {
        return connections;
    }

    @Override
    public boolean isConnected(@NotNull Vertex node) {
        return connections.stream().anyMatch(ch -> ch.getToNode().equals(node));
    }

    @Override
    public Edge getConnection(@NotNull Vertex node) {
        return connections.stream()
                .filter(ch -> ch.getToNode().equals(node))
                .findAny()
                .orElseThrow(() -> new RuntimeException(node + " is not connected"));
    }

    @Override
    public Edge addConnection(@NotNull Edge edge) {
        connections.add(edge);
        return edge;
    }

    @Override
    public Edge addConnectedNode(@NotNull Vertex node, int weight) {
        return factory.createChannel(this, node, weight);
    }

    @Override
    public void removeConnection(@NotNull Edge edge) {
        connections.remove(edge);
    }

    @Override
    public boolean isTerminal() {
        return terminal;
    }

    @Override
    public void setTerminal(boolean value) {
        terminal = value;
    }

    @Override
    public void removeConnectedNode(@NotNull Vertex node) {
        connections.stream()
                .filter(ch -> ch.getToNode().equals(node))
                .findAny()
                .ifPresent(Edge::remove);
    }

    @Override
    public int getOrder() {
        return connections.size();
    }

    @Override
    public String stringRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" [\n");
        connections.forEach(ch -> sb.append("\t")
                .append(ch.stringRepresentation())
                .append("\n"));
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NamedVertex that = (NamedVertex) o;
        return Objects.equals(name, that.name) &&
                toString().equals(that.toString()); // && Objects.equals(connections, that.connections);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, toString()); //, connections);
    }

    @Override
    public String toString() {
        return "Node[name=" + name + ", order=" + getOrder() + "]";
    }
}
