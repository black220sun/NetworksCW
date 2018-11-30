package org.blacksun;

import org.blacksun.graph.NamedGraphNodeFactory;
import org.blacksun.network.Network;
import org.blacksun.network.SimpleTopology;
import org.blacksun.utils.WeightList;

public class Main {
    public static void main(String[] args) {
        WeightList weights = new WeightList(2, 3, 5, 7, 12);
        System.out.println(
            new Network(new SimpleTopology(30, 3, weights, new NamedGraphNodeFactory())).stringRepresentation()
        );
    }
}
