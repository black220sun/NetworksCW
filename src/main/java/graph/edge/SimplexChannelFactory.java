package graph.edge;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

public class SimplexChannelFactory implements ChannelFactory {
    @Override
    public Edge createChannel(@NotNull Vertex from, @NotNull Vertex to, int weight, double errors) {
        return new SimplexChannel(from, to, weight, errors);
    }

    @Override
    public String toString() {
        return "Simplex";
    }
}
