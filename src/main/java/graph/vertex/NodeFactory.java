package graph.vertex;

import graph.edge.ChannelFactory;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

public class NodeFactory extends NamedVertexFactory {
    private final PropertiesHandler cfg = PropertiesHandler.getProps();

    public NodeFactory() {
        super();
    }

    public NodeFactory(@NotNull String name) {
        super(name);
    }

    public NodeFactory(@NotNull String name, @NotNull ChannelFactory factory) {
        super(name, factory);
    }

    @Override
    public Vertex createNode() {
        int counter = cfg.getInt("counter");
        Vertex node = new Node(name + counter, cfg.getProperty("channelFactory"));
        cfg.setProperty("counter", ++counter);
        return node;
    }
}
