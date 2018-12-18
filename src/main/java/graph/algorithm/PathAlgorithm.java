package graph.algorithm;

import graph.edge.Edge;
import graph.vertex.Vertex;
import utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PathAlgorithm implements Algorithm {
    private final List<Vertex> nodes;
    private final List<Edge> links;
    private HashMap<Pair<Vertex, Integer>, Integer> flags;
    private HashMap<Pair<Vertex, Integer>, Vertex> pathMap;

    public PathAlgorithm(@NotNull List<Vertex> nodes, @NotNull List<Edge> links) {
        this.nodes = nodes;
        this.links = links;
    }

    private void computePaths(@NotNull Vertex from) {
        flags = new HashMap<>();
        pathMap = new HashMap<>();
        int size = nodes.size();
        for (Vertex node : nodes) {
            for (int i = 0; i < size; ++i) {
                flags.put(new Pair<>(node, i), INFINITY);
            }
        }
        flags.put(new Pair<>(from, 0), 0);
        for (int i = 1; i < size; ++i) {
            for (Edge edge : links) {
                Pair<Vertex, Integer> vi = new Pair<>(edge.getToNode(), i);
                Vertex fromNode = edge.getFromNode();
                Pair<Vertex, Integer> ui = new Pair<>(fromNode, i - 1);
                Integer oldWeight = flags.get(ui);
                if (oldWeight.equals(INFINITY))
                    continue;
                int newWeight = oldWeight + edge.getWeight();
                if (flags.get(vi) > newWeight) {
                    flags.put(vi, newWeight);
                    pathMap.put(vi, fromNode);
                }
            }
        }
    }

    @Override
    public boolean isAccessible(@NotNull Vertex from, @NotNull Vertex to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return false;
        computePaths(from);
        return flags.keySet()
                .stream()
                .anyMatch(pair -> pair.getFirst().equals(to) && !flags.get(pair).equals(INFINITY));
    }

    @Override
    public int getMinDistance(@NotNull Vertex from, @NotNull Vertex to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return INFINITY;
        computePaths(from);
        return flags.get(minKey(to));
    }

    private Pair<Vertex, Integer> minKey(Vertex to) {
        return flags.keySet()
                .stream()
                .filter(pair -> pair.getFirst().equals(to))
                .min(Comparator.comparingInt(flags::get))
                .orElseThrow(() -> new RuntimeException("Empty graph detected"));
    }

    @Override
    public HashMap<Vertex, Integer> getDistances(@NotNull Vertex node) {
        HashMap<Vertex, Integer> result = new HashMap<>();
        computePaths(node);
        flags.forEach((pair, weight) -> {
            Vertex key = pair.getFirst();
            Integer old = result.get(key);
            if (old == null || old > weight) {
                result.put(key, weight);
            }
        });
        return result;
    }

    @Override
    public GraphPath getPath(@NotNull Vertex from, @NotNull Vertex to) {
        GraphPath path = new GraphPath();
        if (!nodes.contains(from) || !nodes.contains(to))
            return path;
        computePaths(from);
        int size = minKey(to).getSecond();
        Vertex[] nodes = new Vertex[size + 1];
        for (int j = size; j > 0; --j) {
            nodes[j] = to;
            to = pathMap.get(new Pair<>(to, j));
            if (to == null)
                return path;
        }
        nodes[0] = from;
        for (int i = 0; i < size; ++i) {
            path.add(nodes[i].getConnection(nodes[i + 1]));
        }
        return path;
    }
}
