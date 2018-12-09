package org.blacksun.network;

import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.Pair;
import org.blacksun.utils.RandomGenerator;
import org.blacksun.view.Config;
import org.blacksun.view.NetworkPanel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class NetworkSummary {
    private final Network network;
    private final Logger logger;
    private final HashMap<Pair<GraphNode, GraphNode>, Integer> waiting;
    private boolean updated = false;
    private ArrayList<Pair<GraphPath, Integer>> toSend;
    private int time;
    private int packagesSent = 0;
    private int messagesSent = 0;
    private int bytesSent = 0;
    private int createdConnections = 0;
    private final Config cfg;

    public NetworkSummary(@NotNull Network network) {
        this.network = network;
        logger = Logger.getGlobal();
        waiting = new HashMap<>();
        toSend = new ArrayList<>();
        cfg = Config.getConfig();
    }

    private void prepareMessage() {
        int messageSize = cfg.getInt("message");
        RandomGenerator random = new RandomGenerator((int) (messageSize * 1.5),
                (int) (messageSize * 0.5));
        GraphNode fromNode = network.getRandomNode();
        GraphNode toNode;
        do {
            toNode = network.getRandomNode();
        } while (fromNode.equals(toNode));
        int sending = random.next();
        logger.info("Sending " + sending + " bytes from " + fromNode + " to " + toNode);
        GraphPath path = network.getPath(fromNode, toNode, true);
        if (!path.exists()) {
            logger.info("No active connection, creating new");
            path = network.createConnection(fromNode, toNode);
            if (!path.exists()) {
                logger.warning("Can't create new connection. Waiting...");
                waiting.put(new Pair<>(fromNode, toNode), messageSize);
                return;
            }
        } else {
            logger.info("Active connection exists:\n\t" + path);
        }
        toSend.add(computePair(path, messageSize));
    }

    private void tryPrepare() {
        if (updated) {
            ArrayList<Pair<GraphNode, GraphNode>> toRemove = new ArrayList<>();
            waiting.forEach((pair, size) -> {
                GraphPath path = network.createConnection(pair.getFirst(), pair.getSecond());
                if (path.exists()) {
                    logger.info("Created connection:\n\t" + path);
                    toRemove.add(pair);
                    toSend.add(computePair(path, size));
                }
            });
            toRemove.forEach(waiting::remove);
            updated = false;
        }
    }

    private Pair<GraphPath, Integer> computePair(GraphPath path, int messageSize) {
        int size = cfg.getInt("package");
        int amount = (messageSize + size - 1) / size;
        int packages = amount * path.getWeight();
        packagesSent += packages;
        bytesSent += messageSize;
        createdConnections += path.getLength();
        return new Pair<>(path, packages);
    }

    private void send() {
        ArrayList<Pair<GraphPath, Integer>> newToSend = new ArrayList<>();
        toSend.forEach(pair -> {
            int newLeft = pair.getSecond() - 1;
            if (newLeft > 0) {
                newToSend.add(new Pair<>(pair.getFirst(), newLeft));
            } else {
                GraphPath path = pair.getFirst();
                logger.info("Message sent from " + path.getFrom() + " to " +
                        path.getTo() + ". Closing connection");
                messagesSent++;
                updated = true;
                network.closeConnection(path);
            }
        });
        toSend = newToSend;
    }

    private void summary() {
        logger.info("SUMMARY");
        double ticks = time;
        logger.info("Time spent: " + time + "\n" +
                "Messages sent: " + messagesSent + "\t(" + messagesSent / ticks + " per tick)\n" +
                "Packages sent: " + packagesSent + "\t(" + packagesSent / ticks + " per tick)\n" +
                "Bytes sent: " + bytesSent + "\t(" + bytesSent / ticks + " per tick)\n" +
                "Connections created: " + createdConnections + "\t(" + createdConnections / ticks + " per tick)");
    }

    private void iteration(NetworkPanel view, JLabel timeLabel) {
        tryPrepare();
        send();
        time++;
        if (cfg.getBoolean("render")) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
               e.printStackTrace();
            }
            timeLabel.setText(String.valueOf(time));
            view.update();
        }
    }

    public void runTests(@NotNull NetworkPanel view, JLabel timeLabel) {
        int ticks = cfg.getInt("ticks");
        for (int i = 0; i < ticks; ++i) {
            if (i % 50 == 0) {
                for (int j = 0; j < 10; ++j)
                    prepareMessage();
            }
            iteration(view, timeLabel);
        }
        while (!toSend.isEmpty() || !waiting.isEmpty()) {
            iteration(view, timeLabel);
        }
        summary();
    }
}
