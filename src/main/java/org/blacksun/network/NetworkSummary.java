package org.blacksun.network;

import guru.nidi.graphviz.model.MutableGraph;
import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.node.GraphNode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public class NetworkSummary {
    private final Network network;
    private final Logger logger;

    public NetworkSummary(@NotNull Network network) {
        this.network = network;
        logger = Logger.getGlobal();
    }

    public void dumpNetwork() {
        logger.info(network.stringRepresentation());
    }

    public MutableGraph dumpGraph() {
        return network.toGraph();
    }

    public GraphPath createConnection(boolean close) {
        GraphNode from = network.getRandomNode();
        GraphNode to;
        do {
            to = network.getRandomNode();
        } while (to.equals(from));
        String base = "Creating connection from " + from + " to " + to + "\n";
        GraphPath path = network.createConnection(from, to);
        if (path.exists()) {
            logger.info(base + "Success: " + path.toString());
        } else {
            logger.warning(base + "Failed");
        }
        if (close) {
            closeConnection(path);
        }
        return path;
    }

    public void closeConnection(@NotNull GraphPath path) {
        network.closeConnection(path);
        logger.info("Closing connection: " + path);
    }

    public void collectSummary(int cycles) {
        dumpNetwork();
        int nodes = network.getNodes().size();
        int maxConnections = Integer.max(nodes / 3, 12);
        ArrayList<GraphPath> connections = new ArrayList<>();
        for (int i = 0; i < cycles; ++i) {
            logger.info("Cycle" + i);
            connections.addAll(createConnections(maxConnections));
            int forClose = connections.size() / 2;
            closeConnections(connections, forClose);
        }
        logger.info("Closing all connections");
        for (GraphPath connection: connections) {
            closeConnection(connection);
        }
    }

    public List<GraphPath> createConnections(int amount) {
        logger.info("Trying to create " + amount + " connections");
        ArrayList<GraphPath> connections = new ArrayList<>();
        for (int i = 0; i < amount; ++i) {
            GraphPath result = createConnection(false);
            if (result.exists()) {
                connections.add(result);
            }
        }
        return connections;
    }

    public void closeConnections(List<GraphPath> connections, int amount) {
        logger.info("Closing " + amount + " connections");
        Random random = new Random();
        for (int i = 0; i < amount; ++i) {
            GraphPath path = connections.get(random.nextInt(connections.size()));
            closeConnection(path);
            connections.remove(path);
        }
    }
}
