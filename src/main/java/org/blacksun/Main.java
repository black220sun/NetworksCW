package org.blacksun;

import org.blacksun.graph.algorithms.BFAlgorithmFactory;
import org.blacksun.graph.node.NamedGraphNodeFactory;
import org.blacksun.network.Network;
import org.blacksun.network.NetworkSummary;
import org.blacksun.network.SimpleTopology;
import org.blacksun.utils.WeightList;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        WeightList weights = new WeightList(2, 3, 5, 7, 12);
        SimpleTopology topology = new SimpleTopology(30, 3, weights, new NamedGraphNodeFactory());
        BFAlgorithmFactory factory = new BFAlgorithmFactory();
        NetworkSummary summary = new NetworkSummary(new Network(topology, factory));

        FileHandler handler = new FileHandler("summary.log");
        handler.setFormatter(new SimpleFormatter());
        Logger.getGlobal().addHandler(handler);

        summary.collectSummary(5);
    }
}
