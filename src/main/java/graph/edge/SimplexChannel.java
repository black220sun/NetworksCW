package graph.edge;

import graph.vertex.Vertex;

public class SimplexChannel extends AbstractEdge {
    public SimplexChannel(Vertex fromNode, Vertex toNode, int weight, double errors) {
        super(fromNode, toNode, weight, errors);
        connect();
    }

    @Override
    protected String getDirection() {
        return "-->";
    }
}
