package org.blacksun.base;

import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NamedGraphNode implements GraphNode {
    private String name;
    protected List<Channel> connetions;

    public NamedGraphNode(String name) {
        this.name = name == null ? "Node" : name;
        connetions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<GraphNode> getConnectedNodes() {
        return connetions.stream()
                .map(Channel::getToNode)
                .collect(Collectors.toList());
    }

    @Override
    public List<Pair<GraphNode, Integer>> getConnections() {
        return connetions.stream()
                .map(ch -> new Pair<>(ch.getToNode(), ch.getWeight()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isConnected(@NotNull GraphNode node, int distance) {
        // TODO(refactor?)
        if (distance > 1)
            return connetions.stream()
                    .anyMatch(ch -> ch.getToNode().isConnected(node, distance - 1));
        return connetions.stream().anyMatch(ch -> ch.getToNode().equals(node));
    }

    @Override
    public void addConnectedNode(@NotNull GraphNode node, int weight) {
        connetions.add(new Channel(this, node, weight));
    }

    @Override
    public void removeConnectedNode(@NotNull GraphNode node) {
        connetions.removeIf(ch -> ch.getToNode().equals(node));
    }

    @Override
    public int getOrder() {
        return connetions.size();
    }

    @Override
    public String stringRepresentation() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(" [\n");
        connetions.forEach(ch -> sb.append("\t")
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
