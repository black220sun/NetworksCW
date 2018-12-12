package graph.edge;

import graph.vertex.Vertex;
import org.jetbrains.annotations.NotNull;

public class HalfDuplexChannelFactory implements ChannelFactory {
    @Override
    public Edge createChannel(@NotNull Vertex from, @NotNull Vertex to, int weight, double errors) {
        return new HalfDuplexChannel(from, to, weight, errors);
    }

    @Override
    public String toString() {
        return "Half-duplex";
    }
}
