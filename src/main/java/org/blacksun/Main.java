package org.blacksun;

import org.blacksun.base.Network;
import org.blacksun.base.SimpleTopology;
import org.blacksun.utils.WeightList;

public class Main {
    public static void main(String[] args) {
        WeightList weights = new WeightList(2, 3, 5, 7, 12);
        System.out.println(
            new Network(new SimpleTopology(30, 3, weights)).stringRepresentation()
        );
    }
}
