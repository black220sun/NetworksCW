package org.blacksun;

import org.blacksun.graph.algorithms.BFAlgorithmFactory;
import org.blacksun.graph.algorithms.PathFindingAlgorithmFactory;
import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.graph.channel.HalfDuplexChannelFactory;
import org.blacksun.graph.node.GraphNodeFactory;
import org.blacksun.graph.node.SimpleNodeFactory;
import org.blacksun.network.Network;
import org.blacksun.network.SimpleTopology;
import org.blacksun.network.Topology;
import org.blacksun.utils.WeightList;
import org.blacksun.view.MainFrame;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    public static void main(String[] args) throws IOException {
        initLogger();
        new MainFrame(initNetwork());
    }

    private static Network initNetwork() {
        WeightList weights = new WeightList(2, 3, 5, 7, 12);
        ChannelFactory channelFactory = new HalfDuplexChannelFactory();
        GraphNodeFactory nodeFactory = new SimpleNodeFactory("Node", channelFactory);
        Topology topology = new SimpleTopology(30, 3, weights, nodeFactory);
        PathFindingAlgorithmFactory factory = new BFAlgorithmFactory();

        return new Network(topology, factory);
    }

    private static void initLogger() throws IOException {
        FileHandler handler = new FileHandler("summary.log");
        handler.setFormatter(new SimpleFormatter());
        Logger.getGlobal().addHandler(handler);
    }
}
