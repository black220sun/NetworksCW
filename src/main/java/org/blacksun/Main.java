package org.blacksun;

import org.blacksun.base.Network;
import org.blacksun.base.SimpleTopology;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Integer> weights = new ArrayList<>();
        weights.add(2);
        weights.add(3);
        weights.add(5);
        weights.add(7);
        weights.add(12);
        System.out.println(
            new Network(new SimpleTopology(30, 3, weights)).toString()
        );
    }
}
