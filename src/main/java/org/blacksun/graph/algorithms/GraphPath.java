package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class GraphPath implements Iterable<Channel> {
    private final ArrayList<Channel> links;
    private final boolean cycle;

    public GraphPath() {
        this(false);
    }

    public GraphPath(boolean cycle) {
        this.cycle = cycle;
        links = new ArrayList<>();
    }

    public GraphPath add(Channel channel) {
        links.add(channel);
        return this;
    }

    public GraphPath clear() {
        links.clear();
        return this;
    }

    public GraphPath remove() {
        if (!links.isEmpty())
            links.remove(links.size() - 1);
        return this;
    }

    public int getLength() {
        return links.size();
    }

    public int getWeight() {
        return links.stream().mapToInt(Channel::getWeight).sum();
    }

    public boolean exists() {
        return cycle || !links.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Channel> iterator() {
        return links.iterator();
    }

    @Override
    public void forEach(Consumer<? super Channel> action) {
        links.forEach(action);
    }

    @Override
    public Spliterator<Channel> spliterator() {
        return links.spliterator();
    }

    @Override
    public String toString() {
        if (!exists())
            return "";
        if (links.isEmpty())
            return "Access to self";
        StringBuilder base = new StringBuilder();
        base.append("GraphPath[length=")
                .append(getLength())
                .append(", weight=")
                .append(getWeight())
                .append("]:\n\t")
                .append(links.get(0).getFromNode())
                .append(" --> ");
        return base + links.stream()
                .map(Channel::getToNode)
                .map(Object::toString)
                .collect(Collectors.joining(" --> "));
    }

    public GraphNode getFrom() {
        return links.get(0).getFromNode();
    }

    public GraphNode getTo() {
        return links.get(links.size() - 1).getToNode();
    }

    public boolean ofNodes(GraphNode from, GraphNode to) {
        if (from == to && cycle)
            return true;
        if (links.isEmpty())
            return false;
        return getFrom().equals(from) &&
                getTo().equals(to);
    }

    // sending package in datagram mode
    public void close(int weight) {
        int sum = 0;
        for (int i = links.size() - 1; i >= 0; --i) {
            Channel channel = links.get(i);
            if (weight > sum) {
                sum += channel.getWeight();
            } else {
                channel.setUsed(false);
            }
        }
    }
}
