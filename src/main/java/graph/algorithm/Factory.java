package graph.algorithm;

import graph.edge.Edge;
import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Factory {
    Algorithm getAlgorithm(@NotNull List<Vertex> nodes,
                           @NotNull List<Edge> links);
}
