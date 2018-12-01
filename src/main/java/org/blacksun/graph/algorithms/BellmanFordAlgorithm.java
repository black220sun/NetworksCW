package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BellmanFordAlgorithm implements PathFindingAlgorithm {
    private final List<GraphNode> nodes;
    private final List<Channel> links;

    public BellmanFordAlgorithm(@NotNull List<GraphNode> nodes, @NotNull List<Channel> links) {
        this.nodes = nodes;
        this.links = links;
    }

    private HashMap<Pair<GraphNode, Integer>, Integer> computePaths(@NotNull GraphNode from) {
        HashMap<Pair<GraphNode, Integer>, Integer> map = new HashMap<>();
        int size = nodes.size();
        for (GraphNode node : nodes) {
            for (int i = 0; i < size; ++i) {
                map.put(new Pair<>(node, i), INFINITY);
            }
        }
        map.put(new Pair<>(from, 0), 0);
        for (int i = 1; i < size; ++i) {
            for (Channel channel : links) {
                Pair<GraphNode, Integer> vi = new Pair<>(channel.getToNode(), i);
                Pair<GraphNode, Integer> ui = new Pair<>(channel.getFromNode(), i - 1);
                Integer oldWeight = map.get(ui);
                if (oldWeight.equals(INFINITY))
                    continue;
                int newWeight = oldWeight + channel.getWeight();
                if (map.get(vi) > newWeight) {
                    map.put(vi, newWeight);
                }
            }
        }
        return map;
    }

    @Override
    public boolean isAccessible(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return false;
        HashMap<Pair<GraphNode, Integer>, Integer> map = computePaths(from);
        return map.keySet()
                .stream()
                .anyMatch(pair -> pair.getFirst().equals(to) && !map.get(pair).equals(INFINITY));
    }

    @Override
    public int getMinDistance(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return INFINITY;
        HashMap<Pair<GraphNode, Integer>, Integer> map = computePaths(from);
        Pair<GraphNode, Integer> key = map.keySet()
                .stream()
                .filter(pair -> pair.getFirst().equals(to))
                .min(Comparator.comparingInt(map::get))
                .orElseThrow(() -> new RuntimeException("Empty graph detected"));
        return map.get(key);
    }

    @Override
    public HashMap<GraphNode, Integer> getDistances(@NotNull GraphNode node) {
        HashMap<GraphNode, Integer> map = new HashMap<>();
        computePaths(node).forEach((pair, weight) -> {
            GraphNode key = pair.getFirst();
            Integer old = map.get(key);
            if (old == null || old > weight) {
                map.put(key, weight);
            }
        });
        return map;
    }

    @Override
    public GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to) {
        GraphPath path = new GraphPath();
        if (!nodes.contains(from) || !nodes.contains(to))
            return path;
        throw new NotImplementedException();
    }
}
