package network;

import graph.algorithm.GraphPath;
import graph.vertex.Vertex;
import utils.Pair;
import utils.IntGenerator;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Logger;

public class TestRunner {
    private final Network network;
    private final Logger logger;
    private final ArrayList<Pair<Pair<Vertex, Vertex>, Integer>> waiting;
    private boolean updated = true;
    private ArrayList<Pair<GraphPath, Integer>> toSend;
    private int time;
    private int packagesSent = 0;
    private int utilSent = 0;
    private int messagesSent = 0;
    private int bytesSent = 0;
    private int createdConnections = 0;
    private static final PropertiesHandler cfg = PropertiesHandler.getProps();

    public TestRunner(@NotNull Network network) {
        this.network = network;
        logger = Logger.getGlobal();
        waiting = new ArrayList<>();
        toSend = new ArrayList<>();
    }

    public static String getConfigOptions() {
        return "Тип каналу: " + cfg.getProperty("channelFactory") + "\n" +
                "Середній розмір повідомлення: " + cfg.getInt("message") + " байтів\n" +
                "Розмір інформаційного пакету: " + cfg.getInt("package") + " байтів\n" +
                "Розмір службового пакету: " + cfg.getInt("utility") + " байтів\n" +
                "Затримка генерації повідомлення: " + cfg.getInt("delay") + " тактів\n";
    }

    private void prepareMessage(boolean datagram) {
        int messageSize = cfg.getInt("message");
        IntGenerator random = new IntGenerator((int) (messageSize * 1.5),
                (int) (messageSize * 0.5));
        Vertex fromNode = network.getRandomNode(true);
        Vertex toNode;
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
            // edge initiation
            utilSent += 4;
        }
        waiting.add(new Pair<>(new Pair<>(fromNode, toNode), sending));
        messagesSent++;
    }

    private void tryPrepare() {
        if (updated) {
            ArrayList<Pair<Pair<Vertex, Vertex>, Integer>> toRemove = new ArrayList<>();
            waiting.forEach(entry -> {
                Pair<Vertex, Vertex> pair = entry.getFirst();
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
        utilSent += amount;
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
        bytesSent += cfg.getInt("utility") * utilSent;
        String results = "Результати:\n" +
                time + " тактів\nВідправлено:\n" +
                messagesSent + " повідомлень\n" +
                packagesSent + " інформаційних пакетів\n" +
                utilSent + " службових пакетів\n" +
                bytesSent + " байтів" + "\nШвидкість:\n" +
                bytesSent / ticks + " байтів/такт" + "\n" +
                messagesSent / ticks + " повідомлень/такт\n" +
                packagesSent / ticks + " інф. пакетів/такт\n\n" +
                createdConnections + " каналів використано\n";
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
