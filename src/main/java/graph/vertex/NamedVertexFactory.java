package graph.vertex;

import graph.edge.ChannelFactory;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

public class NamedVertexFactory implements VertexFactory {
    protected final String name;
    protected final ChannelFactory factory;
    protected int nodesCounter;

    public NamedVertexFactory() {
        this("Node");
    }

    public NamedVertexFactory(@NotNull String name) {
        this(name, PropertiesHandler.getProps().getProperty("channelFactory"));
    }

    public NamedVertexFactory(@NotNull String name, @NotNull ChannelFactory factory) {
        this.name = name;
        this.factory = factory;
        nodesCounter = 0;
    }

    @Override
    public Vertex createNode() {
        return new NamedVertex(name + nodesCounter++, factory);
    }
}
