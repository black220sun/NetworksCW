package graph.edge;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

public interface ChannelFactory {
    default Edge createChannel(@NotNull Vertex from, @NotNull Vertex to, int weight) {
        return createChannel(from, to, weight, Edge.ERRORS_AMOUNT);
    }

    Edge createChannel(@NotNull Vertex from, @NotNull Vertex to, int weight, double errors);
}
