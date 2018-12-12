package graph.algorithm;

import graph.edge.Edge;
import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class GraphPath implements Iterable<Edge> {
    private final ArrayList<Edge> links;
    private final boolean cycle;

    public GraphPath() {
        this(false);
    }

    public GraphPath(boolean cycle) {
        this.cycle = cycle;
        links = new ArrayList<>();
    }

    public GraphPath add(Edge edge) {
        links.add(edge);
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
        return links.stream().mapToInt(Edge::getWeight).sum();
    }

    public boolean exists() {
        return cycle || !links.isEmpty();
    }

    @NotNull
    @Override
    public Iterator<Edge> iterator() {
        return links.iterator();
    }

    @Override
    public void forEach(Consumer<? super Edge> action) {
        links.forEach(action);
    }

    @Override
    public Spliterator<Edge> spliterator() {
        return links.spliterator();
    }

    @Override
    public String toString() {
        if (!exists())
            return "No path";
        if (links.isEmpty())
            return "Cycle";
        return "[" + getWeight() + "] " + getFrom() + ">" +
                links.stream()
                .map(Edge::getToNode)
                .map(Object::toString)
                .collect(Collectors.joining(">"));
    }

    public Vertex getFrom() {
        return links.get(0).getFromNode();
    }

    public Vertex getTo() {
        return links.get(links.size() - 1).getToNode();
    }

    public boolean ofNodes(Vertex from, Vertex to) {
        if (from == to && cycle)
            return true;
        if (links.isEmpty())
            return false;
        return getFrom().equals(from) &&
                getTo().equals(to);
    }

    // sending package in datagram mode
    public boolean close(int weight) {
        int sum = 0;
        boolean result = false;
        for (int i = links.size() - 1; i >= 0; --i) {
            Edge edge = links.get(i);
            if (weight > sum) {
                sum += edge.getWeight();
            } else {
                edge.setUsed(false);
                result = true;
            }
        }
        return result;
    }
}
