package network;

import graph.vertex.Vertex;
import graph.vertex.VertexFactory;
import utils.PropertiesHandler;
import utils.WeightList;

import java.util.List;

public class RegionalTopology implements Topology {
    private final int nodes;
    private final double order;
    private final int n;
    private final WeightList weights;
    private final VertexFactory factory;

    public RegionalTopology(int nodes, double order, int n,
                            WeightList weights, VertexFactory nodeFactory) {
        this.nodes = nodes;
        this.order = order;
        this.n = n;
        this.weights = weights;
        this.factory = nodeFactory;
    }

    @Override
    public List<Vertex> createNetwork() {
        Topology reg1 = new SimpleTopology(nodes, order, n, weights, factory);
        Topology reg2 = new SimpleTopology(nodes, order, n, weights, factory);
        List<Vertex> n1 = reg1.createNetwork();
        List<Vertex> n2 = reg2.createNetwork();
        n1.get(nodes / 2).addConnectedNode(n2.get(nodes / 2),
                PropertiesHandler.getProps().<WeightList>getProperty("weights").getWeight());
        n1.addAll(n2);
        return n1;
    }

    @Override
    public Vertex createNode() {
        return factory.createNode();
    }
}
