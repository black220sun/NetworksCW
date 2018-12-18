package graph.edge;

import graph.vertex.Vertex;

public class HalfDuplexChannel extends AbstractEdge {
    private HalfDuplexChannel pair;

    private HalfDuplexChannel(HalfDuplexChannel channel) {
        super(channel.toNode, channel.fromNode, channel.weight, channel.errors);
        pair = channel;
    }

    public HalfDuplexChannel(Vertex fromNode, Vertex toNode, int weight, double errors) {
        super(fromNode, toNode, weight, errors);
        pair = new HalfDuplexChannel(this);
        connect();
    }

    @Override
    public void setUsed(boolean used) {
        this.used = used;
        pair.used = used;
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
        return "<-->";
    }
}