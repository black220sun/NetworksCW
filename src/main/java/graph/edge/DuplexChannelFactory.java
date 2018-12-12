package graph.edge;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

public class DuplexChannelFactory implements ChannelFactory {
    @Override
    public Edge createChannel(@NotNull Vertex from, @NotNull Vertex to, int weight, double errors) {
        return new DuplexChannel(from, to, weight, errors);
    }

    @Override
    public String toString() {
        return "Duplex";
    }
}
