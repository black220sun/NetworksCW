package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;

public class DuplexChannel extends AbstractChannel {
    protected DuplexChannel pair;

    private DuplexChannel(DuplexChannel channel) {
        super(channel.toNode, channel.fromNode, channel.weight, channel.errors);
        pair = channel;
    }

    public DuplexChannel(GraphNode fromNode, GraphNode toNode, int weight, double errors) {
        super(fromNode, toNode, weight, errors);
        pair = new DuplexChannel(this);
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
