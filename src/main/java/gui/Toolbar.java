package gui;

import graph.algorithm.GraphPath;
import graph.edge.*;
import graph.vertex.Vertex;
import network.Network;
import network.TestRunner;
import utils.PropertiesHandler;
import utils.WeightList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class Toolbar extends JPanel {
    private final NetworkPanel networkPanel;
    private final Network network;
    private final JComboBox<Vertex> fromNode;
    private final JComboBox<Vertex> toNode;
    private final JTextArea infoArea;

    public Toolbar(NetworkPanel panel) {
        super();
        this.networkPanel = panel;
        network = panel.getNetwork();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        Vertex[] nodes = network.getNodes(false).toArray(new Vertex[]{});
        fromNode = new JComboBox<>(nodes);
        toNode = new JComboBox<>(nodes);
        panel1();
        panel2();
        panel3();
        infoArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(infoArea);
        jScrollPane.getViewport().setPreferredSize(new Dimension(400, 800));
        add(jScrollPane);
    }

    private void panel3() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createButton("Connect", nodeAction(network::createConnection)));
        panel.add(createButton("Disconnect", nodeAction(network::closeConnection)));
        panel.add(createButton("Close all", e -> {
            network.closeAll();
            networkPanel.update();
        }));
        panel.add(createButton("Show table", e -> {
            Vertex node = (Vertex) fromNode.getSelectedItem();
            infoArea.setText(network.getPaths(node).stream()
                    .map(GraphPath::toString)
                    .collect(Collectors.joining("\n")));
        }));
        add(panel);
    }

    private void panel2() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(createButton("Terminal", nodeAction((n1, n2) -> {
            n1.setTerminal(!n1.isTerminal());
            updateNodes();
        })));
        pane.add(createButton("Add", e -> {
            network.addNode();
            updateNodes();
            networkPanel.update();
        }));
        pane.add(createButton("Remove", nodeAction((node, n) -> {
            network.removeNode(node);
            updateNodes();
        })));
        pane.add(createButton("Link", nodeAction((n1, n2) -> {
            PropertiesHandler cfg = PropertiesHandler.getProps();
            int weight = cfg.<WeightList>getProperty("weights").getWeight();
            cfg.<ChannelFactory>getProperty("channelFactory").createChannel(n1, n2, weight);
        })));
        pane.add(createButton("Unlink", nodeAction(network::removeConnection)));
        add(pane);
    }

    private void panel1() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(fromNode);
        panel.add(toNode);
        panel.add(createButton("Remove all", e -> {
            network.clear();
            updateNodes();
            networkPanel.update();
        }));
        panel.add(createButton("Generate", e -> {
            PropertiesHandler.getProps().setProperty("counter", 0);
            network.generateNetwork();
            updateNodes();
            networkPanel.update();
        }));
        panel.add(createButton("Run test", e -> {
            infoArea.setText("");
            network.closeAll();
            infoArea.setText(TestRunner.getConfigOptions() +
                    "\nVirtual chanel\n" +
                    new TestRunner(network).runTests(false) +
                    "\nDatagram mode\n" +
                    new TestRunner(network).runTests(true));
        }));
        add(panel);
    }

    private Component createButton(String name, ActionListener action) {
        JButton button = new JButton(name);
        button.addActionListener(action);
        return button;
    }

    private void updateNodes() {
        fromNode.removeAllItems();
        toNode.removeAllItems();
        network.getNodes(false).forEach(node -> {
            fromNode.addItem(node);
            toNode.addItem(node);
        });
    }

    private ActionListener nodeAction(BiConsumer<Vertex, Vertex> consumer) {
        return e -> {
            Vertex from = (Vertex) fromNode.getSelectedItem();
            Vertex to = (Vertex) toNode.getSelectedItem();
            consumer.accept(from, to);
            networkPanel.update();
        };
    }
}
