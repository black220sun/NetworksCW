package graph.algorithm;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface Algorithm {
    Integer INFINITY = Integer.MAX_VALUE;

    boolean isAccessible(@NotNull Vertex from, @NotNull Vertex to);
    int getMinDistance(@NotNull Vertex from, @NotNull Vertex to);
    HashMap<Vertex, Integer> getDistances(@NotNull Vertex node);
    GraphPath getPath(@NotNull Vertex from, @NotNull Vertex to);
}
