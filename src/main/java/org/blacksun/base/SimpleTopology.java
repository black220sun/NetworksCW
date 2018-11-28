package org.blacksun.base;

import org.blacksun.utils.RandomGenerator;
import org.blacksun.utils.WeightList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleTopology implements Topology {
    private final int amount;
    private final int order;
    private final WeightList weights;

    public SimpleTopology(int amount, double order, @NotNull WeightList weights) {
        if (amount <= 0 || order <= 0)
            throw new IllegalArgumentException();
        this.amount = amount;
        this.order = (int) (order * 2);
        this.weights = weights;
    }

    @Override
    public List<GraphNode> createNetwork() {
        List<GraphNode> list = new ArrayList<>();
        // create all nodes
        for (int i = 0; i < amount; ++i)
            list.add(new NamedGraphNode("Node" + i));
        // link nodes
        RandomGenerator orderGen = new RandomGenerator(order, 1);
        RandomGenerator indexGen = new RandomGenerator(amount);
        for (int index = 0; index < amount; ++index) {
            GraphNode node = list.get(index);
            int thisOrder = orderGen.next();
            for (int i = node.getConnectedNodes().size(); i < thisOrder; i++) {
                node.addConnectedNode(list.get(indexGen.nextExcept(index)),
                        weights.getWeight());
            }
        }
        return list;
    }
}
