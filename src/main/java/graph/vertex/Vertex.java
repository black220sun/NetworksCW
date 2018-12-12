package graph.vertex;

import graph.edge.Edge;
import utils.Selectable;
import utils.Stringable;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface Vertex extends Stringable, Selectable {
    List<Vertex> getConnectedNodes();
    // returns list or connected nodes with the weight of connection
    List<Edge> getConnections();
    boolean isConnected(@NotNull Vertex node);
    Edge getConnection(@NotNull Vertex node);
    Edge addConnectedNode(@NotNull Vertex node, int weight);
    Edge addConnection(@NotNull Edge edge);
    void removeConnectedNode(@NotNull Vertex node);
    void removeConnection(@NotNull Edge edge);
    boolean isTerminal();
    void setTerminal(boolean value);
    int getOrder();
}
