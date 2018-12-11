package org.blacksun.network;

import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.utils.Pair;
import org.blacksun.utils.RandomGenerator;
import org.blacksun.utils.Config;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

public class NetworkSummary {
    private final Network network;
    private final Logger logger;
    private final ArrayList<Pair<Pair<GraphNode, GraphNode>, Integer>> waiting;
    private boolean updated = true;
    private ArrayList<Pair<GraphPath, Integer>> toSend;
    private int time;
    private int packagesSent = 0;
    private int messagesSent = 0;
    private int bytesSent = 0;
    private int createdConnections = 0;
    private static final Config cfg = Config.getConfig();

    public NetworkSummary(@NotNull Network network) {
        this.network = network;
        logger = Logger.getGlobal();
        waiting = new ArrayList<>();
        toSend = new ArrayList<>();
    }

    public static String getConfigOptions() {
        return "ENVIRONMENT\n" +
                "Default channel type: " + cfg.getProperty("channelFactory") + "\n" +
                "Base time: " + cfg.getInt("ticks") + "\n" +
                "Average message size: " + cfg.getInt("message") + "\n" +
                "Package size: " + cfg.getInt("package") + "\n" +
                "Message appearance delay: " + cfg.getInt("delay") + "\n" +
                "Message appearance amount: " + cfg.getInt("amount");
    }

    private void prepareMessage(boolean datagram) {
        int messageSize = cfg.getInt("message");
        RandomGenerator random = new RandomGenerator((int) (messageSize * 1.5),
                (int) (messageSize * 0.5));
        GraphNode fromNode = network.getRandomNode(true);
        GraphNode toNode;
        do {
            toNode = network.getRandomNode(true);
        } while (fromNode.equals(toNode));
        int sending = random.next();
        logger.info("Sending " + sending + " bytes from " + fromNode + " to " + toNode);
        if (datagram) {
            int packageSize = cfg.getInt("package");
            while (sending > packageSize) {
                waiting.add(new Pair<>(new Pair<>(fromNode, toNode), packageSize));
                sending -= packageSize;
            }
        } else {
            // channel initiation
            bytesSent += cfg.getInt("utility");
            packagesSent++;
        }
        waiting.add(new Pair<>(new Pair<>(fromNode, toNode), sending));
        messagesSent++;
    }

    private void tryPrepare() {
        if (updated) {
            ArrayList<Pair<Pair<GraphNode, GraphNode>, Integer>> toRemove = new ArrayList<>();
            waiting.forEach(entry -> {
                Pair<GraphNode, GraphNode> pair = entry.getFirst();
                int size = entry.getSecond();
                GraphPath path = network.createConnection(pair.getFirst(), pair.getSecond());
                if (path.exists()) {
                    logger.info("Created connection:\n\t" + path);
                    toRemove.add(entry);
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
        int ticks = amount * path.getWeight();
        logger.info("Created " + amount + " package(s). Time to deliver: " + ticks + " ticks");
        packagesSent += amount;
        bytesSent += messageSize;
        createdConnections += path.getLength();
        return new Pair<>(path, ticks);
    }

    private void send(boolean datagram) {
        ArrayList<Pair<GraphPath, Integer>> newToSend = new ArrayList<>();
        toSend.forEach(pair -> {
            int newLeft = pair.getSecond() - 1;
            GraphPath path = pair.getFirst();
            if (datagram) {
                updated = path.close(newLeft);
            }
            if (newLeft > 0) {
                newToSend.add(new Pair<>(path, newLeft));
            } else {
                String what = datagram ? "Package" : "Message";
                logger.info(what + " sent from " + path.getFrom() + " to " +
                        path.getTo() + ". Closing connection");
                updated = true;
                if (!datagram) {
                    network.closeConnection(path);
                }
            }
        });
        toSend = newToSend;
    }

    private String summary() {
        double ticks = time;
        double msg = messagesSent;
        String results = "SUMMARY\n" +
                "Time spent: " + time + " ticks\n" +
                "Messages sent: " + messagesSent + "\n" +
                "Messages sent per tick: " + messagesSent / ticks + "\n" +
                "Ticks per message: " + time / msg + "\n" +
                "Packages sent: " + packagesSent + "\n" +
                "Packages sent per tick: " + packagesSent / ticks + "\n" +
                "Ticks per package: " + ticks / packagesSent + "\n" +
                "Packages sent per message: " + packagesSent / msg + "\n" +
                "Bytes sent: " + bytesSent + "\n" +
                "Bytes sent per tick: " + bytesSent / ticks + "\n" +
                "Bytes sent per message: " + bytesSent / msg + "\n" +
                "Connections created: " + createdConnections + "\n" +
                "Connections created per tick: " + createdConnections / ticks + "\n" +
                "Connections created per message: " + createdConnections / msg;
        logger.info(results);
        return results;
    }

    private void iteration(boolean datagram) {
        tryPrepare();
        send(datagram);
        time++;
    }

    public String runTests(boolean datagram) {
        int ticks = cfg.getInt("ticks");
        int delay = cfg.getInt("delay");
        int amount = cfg.getInt("amount");
        for (int i = 0; i < ticks; ++i) {
            if (i % delay == 0) {
                for (int j = 0; j < amount; ++j)
                    prepareMessage(datagram);
            }
            iteration(datagram);
        }
        while (!toSend.isEmpty() || !waiting.isEmpty()) {
            iteration(datagram);
        }
        return summary();
    }
}
