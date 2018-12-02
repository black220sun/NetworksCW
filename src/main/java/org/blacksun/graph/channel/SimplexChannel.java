package org.blacksun.graph.channel;

import org.blacksun.graph.node.GraphNode;

public class SimplexChannel extends AbstractChannel {
    public SimplexChannel(GraphNode fromNode, GraphNode toNode, int weight, double errors) {
        super(fromNode, toNode, weight, errors);
        connect();
    }

    @Override
    protected String getDirection() {
        return "-->";
    }
}
