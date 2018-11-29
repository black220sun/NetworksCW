package org.blacksun.base;

import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NamedGraphNode implements GraphNode {
    private String name;
    protected List<Channel> connections;

    public NamedGraphNode(@NotNull String name) {
        this.name = name;
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
    public void addConnection(@NotNull Channel channel) {
        connections.add(channel);
    }

    @Override
    public void addConnectedNode(@NotNull GraphNode node, int weight) {
        Channel channel = new Channel(this, node, weight);
        connections.add(channel);
        // Type.DUPLEX by default
        node.addConnection(channel.reversed());
    }

    @Override
    public void removeConnection(@NotNull Channel channel) {
        connections.remove(channel);
    }

    @Override
    public void removeConnectedNode(@NotNull GraphNode node) {
        connections.stream()
                .filter(ch -> ch.getToNode().equals(node))
                .findAny().ifPresent(ch -> {
                    connections.remove(ch);
                    if (ch.getType().equals(Channel.Type.DUPLEX)) {
                        ch.getToNode().removeConnection(ch.reversed());
                    }
                });
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
    public String toString() {
        return "Node[name=" + name + ", order=" + getOrder() + "]";
    }
}
