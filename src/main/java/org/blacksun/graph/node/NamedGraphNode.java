package org.blacksun.graph.node;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class NamedGraphNode implements GraphNode {
    private String name;
    private final ChannelFactory factory;
    protected List<Channel> connections;

    NamedGraphNode(@NotNull String name, @NotNull ChannelFactory factory) {
        this.name = name;
        this.factory = factory;
        connections = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    @Override
    public List<GraphNode> getConnectedNodes() {
        return connections.stream()
                .map(Channel::getToNode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair<GraphNode, Integer>> getConnections() {
        return connections.stream()
                .map(ch -> new Pair<>(ch.getToNode(), ch.getWeight()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConnected(@NotNull GraphNode node) {
        return connections.stream().anyMatch(ch -> ch.getToNode().equals(node));
    }

    @Override
    public Channel addConnection(@NotNull Channel channel) {
        connections.add(channel);
        return channel;
    }

    @Override
    public Channel addConnectedNode(@NotNull GraphNode node, int weight) {
        Channel channel = factory.createChannel(this, node, weight);
        channel.connect();
        return channel;
    }

    @Override
    public void removeConnection(@NotNull Channel channel) {
        connections.remove(channel);
    }

    @Override
    public void removeConnectedNode(@NotNull GraphNode node) {
        connections.stream()
                .filter(ch -> ch.getToNode().equals(node))
                .findAny().ifPresent(Channel::remove);
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
        NamedGraphNode that = (NamedGraphNode) o;
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