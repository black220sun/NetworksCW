package org.blacksun.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleTopology implements Topology {
    private final int amount;
    private final int order;
    private final List<Integer> weights;
    private final Random randomGen = new Random();
    private final int weightsSize;

    public SimpleTopology(int amount, double order, List<Integer> weights) {
        if (amount <= 0 || order <= 0)
            throw new IllegalArgumentException();
        this.amount = amount;
        this.order = (int) (order * 2) - 1;
        this.weights = weights;
        weightsSize = weights.size();
    }

    @Override
    public List<GraphNode> createNetwork() {
        List<GraphNode> list = new ArrayList<>();
        // create all nodes
        for (int i = 0; i < amount; ++i)
            list.add(new NamedGraphNode("Node" + i));
        // link nodes
        int randomIndex;
        for (int index = 0; index < amount; ++index) {
            GraphNode node = list.get(index);
            int thisOrder = randomGen.nextInt(order) + 1; // to prevent unconnected nodes
            for (int i = node.getConnectedNodes().size(); i < thisOrder; i++) {
                do {
                    randomIndex = randomGen.nextInt(amount);
                } while (randomIndex == index);
                node.addConnectedNode(list.get(randomIndex), getWeight());
            }
        }
        return list;
    }

    private int getWeight() {
        return weights.get(randomGen.nextInt(weightsSize));
    }
}
