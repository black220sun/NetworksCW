package graph.algorithm;

import graph.edge.Edge;
import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DejkstraFactory implements Factory {
    @Override
    public Algorithm getAlgorithm(@NotNull List<Vertex> nodes,
                                  @NotNull List<Edge> links) {
        return new DejkstraAlgorithm(nodes, links);
    }
}
