package org.blacksun.graph.algorithms;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class BellmanFordAlgorithm implements PathFindingAlgorithm {
    private final List<GraphNode> nodes;
    private final List<Channel> links;
    private HashMap<Pair<GraphNode, Integer>, Integer> map;
    private HashMap<Pair<GraphNode, Integer>, GraphNode> pathMap;
    private GraphNode last;

    public BellmanFordAlgorithm(@NotNull List<GraphNode> nodes, @NotNull List<Channel> links) {
        this.nodes = nodes;
        this.links = links;
        last = null;
    }

    private void computePaths(@NotNull GraphNode from) {
        if (from.equals(last)) // already computed, return cached
            return;
        last = from;
        map = new HashMap<>();
        pathMap = new HashMap<>();
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
                GraphNode fromNode = channel.getFromNode();
                Pair<GraphNode, Integer> ui = new Pair<>(fromNode, i - 1);
                Integer oldWeight = map.get(ui);
                if (oldWeight.equals(INFINITY))
                    continue;
                int newWeight = oldWeight + channel.getWeight();
                if (map.get(vi) > newWeight) {
                    map.put(vi, newWeight);
                    pathMap.put(vi, fromNode);
                }
            }
        }
    }

    @Override
    public boolean isAccessible(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return false;
        computePaths(from);
        return map.keySet()
                .stream()
                .anyMatch(pair -> pair.getFirst().equals(to) && !map.get(pair).equals(INFINITY));
    }

    @Override
    public int getMinDistance(@NotNull GraphNode from, @NotNull GraphNode to) {
        if (!nodes.contains(from) || !nodes.contains(to))
            return INFINITY;
        computePaths(from);
        return map.get(minKey(to));
    }

    private Pair<GraphNode, Integer> minKey(GraphNode to) {
        return map.keySet()
                .stream()
                .filter(pair -> pair.getFirst().equals(to))
                .min(Comparator.comparingInt(map::get))
                .orElseThrow(() -> new RuntimeException("Empty graph detected"));
    }

    @Override
    public HashMap<GraphNode, Integer> getDistances(@NotNull GraphNode node) {
        HashMap<GraphNode, Integer> result = new HashMap<>();
        computePaths(node);
        map.forEach((pair, weight) -> {
            GraphNode key = pair.getFirst();
            Integer old = result.get(key);
            if (old == null || old > weight) {
                result.put(key, weight);
            }
        });
        return result;
    }

    @Override
    public GraphPath getPath(@NotNull GraphNode from, @NotNull GraphNode to) {
        GraphPath path = new GraphPath();
        if (!nodes.contains(from) || !nodes.contains(to))
            return path;
        computePaths(from);
        int size = minKey(to).getSecond();
        GraphNode[] nodes = new GraphNode[size + 1];
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
