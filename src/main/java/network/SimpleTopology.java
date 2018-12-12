package network;

import graph.vertex.Vertex;
import graph.vertex.VertexFactory;
import utils.IntGenerator;
import utils.WeightList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleTopology implements Topology {
    private final int amount;
    private final int order;
    private final int lower;
    private final WeightList weights;
    private final VertexFactory factory;
    private final int terminal;

    public SimpleTopology(int amount, double order, int terminal,
                          @NotNull WeightList weights, @NotNull VertexFactory factory) {
        if (amount < 0)
            throw new IllegalArgumentException("Amount must be non-negative");
        if (order <= 0)
            throw new IllegalArgumentException("Order must be greater than 0");
        this.amount = amount;
        this.order = (int) (order * 1.75);
        lower = this.order > 1 ? 1 : 0;
        this.weights = weights;
        this.factory = factory;
        this.terminal = terminal;
    }

    @Override
    public List<Vertex> createNetwork() {
        List<Vertex> nodes = new ArrayList<>();
        // create all nodes
        for (int i = 0; i < amount; ++i) {
            Vertex node = factory.createNode();
            if (i % terminal == 0) {
                node.setTerminal(true);
            }
            nodes.add(node);
        }
        IntGenerator orderGen = new IntGenerator(order, lower);
        IntGenerator indexGen = new IntGenerator(amount);
        Vertex node, other;
        int thisOrder, currentOrder;
        // link nodes
        for (int index = 0; index < amount; ++index) {
            // TODO (preserve correct order for already processed nodes)
            // current implementation creates slightly more links (see test class)
            node = nodes.get(index);
            thisOrder = orderGen.next();
            currentOrder = node.getConnectedNodes().size();
            while (currentOrder < thisOrder) {
                other = nodes.get(indexGen.nextExcept(index));
                if (!node.isConnected(other)) {
                    node.addConnectedNode(other, weights.getWeight());
                    ++currentOrder;
                }
            }
        }
        return nodes;
    }

    @Override
    public Vertex createNode() {
        return factory.createNode();
    }
}
