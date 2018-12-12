package graph.edge;

import graph.vertex.Vertex;

public class DuplexChannel extends AbstractEdge {
    private DuplexChannel pair;

    private DuplexChannel(DuplexChannel channel) {
        super(channel.toNode, channel.fromNode, channel.weight, channel.errors);
        pair = channel;
    }

    public DuplexChannel(Vertex fromNode, Vertex toNode, int weight, double errors) {
        super(fromNode, toNode, weight, errors);
        pair = new DuplexChannel(this);
        connect();
    }

    @Override
    public void connect() {
        super.connect();
        toNode.addConnection(pair);
    }

    @Override
    public void remove() {
        super.remove();
        toNode.removeConnection(pair);
    }

    @Override
    protected String getDirection() {
        return "<==>";
    }
}
