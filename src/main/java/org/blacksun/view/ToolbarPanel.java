package org.blacksun.view;

import org.blacksun.graph.algorithms.GraphPath;
import org.blacksun.graph.channel.*;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.graph.node.NamedGraphNode;
import org.blacksun.network.Network;
import org.blacksun.network.NetworkSummary;
import org.blacksun.utils.Config;
import org.blacksun.utils.WeightList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ToolbarPanel extends JPanel {
    private final NetworkPanel networkPanel;
    private final Network network;
    private final JCheckBox terminal;
    private final JComboBox<GraphNode> fromNode;
    private final JComboBox<GraphNode> toNode;
    private final JLabel timeLabel;
    private final JTextArea infoArea;
    private int time;

    public ToolbarPanel(NetworkPanel panel) {
        super();
        this.networkPanel = panel;
        network = panel.getNetwork();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GraphNode[] nodes = network.getNodes(true).toArray(new GraphNode[]{});
        fromNode = new JComboBox<>(nodes);
        toNode = new JComboBox<>(nodes);
        terminal = new JCheckBox("Terminal", true);
        addNodePanel();
        addChangePanel();
        addInfoPanel();
        timeLabel = new JLabel("0");
        addTimePanel();
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        add(infoArea);
    }

    private void addInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createButton("Remove all", e -> {
            network.clear();
            updateNodes();
            terminal.setSelected(false);
            networkPanel.update();
        }));
        panel.add(createButton("Generate", e -> {
            Config.getConfig().setProperty("counter", 0);
            network.generateNetwork();
            updateNodes();
            networkPanel.update();
        }));
        panel.add(createButton("Show table", e -> {
            GraphNode node = (GraphNode) fromNode.getSelectedItem();
            infoArea.setText(network.getPaths(node).stream()
                    .map(GraphPath::toString)
                    .collect(Collectors.joining("\n")));
        }));
        panel.add(createButton("Run test", e -> {
            infoArea.setText("");
            network.closeAll();
            infoArea.setText("\t" + NetworkSummary.getConfigOptions() +
                    "\n\tVIRTUAL CHANNEL MODE " +
                    new NetworkSummary(network).runTests(false) +
                    "\n\tDATAGRAM MODE " +
                    new NetworkSummary(network).runTests(true));
        }));
        panel.add(createButton("Clear", e -> infoArea.setText("")));
        add(panel);
    }

    private void addTimePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(timeLabel);
        panel.add(new JLabel("        "));
        panel.add(createButton("Reset", e -> {
            timeLabel.setText("0");
            time = 0;
        }));
        panel.add(createButton("Send", nodeAction((n1, n2) -> {
            time += network.getPath(n1, n2, true).getWeight();
            timeLabel.setText(String.valueOf(time));
        })));
        panel.add(createButton("Settings", e -> new SettingsFrame()));
        panel.add(createButton("Update", e -> networkPanel.update()));
        add(panel);
    }

    private void addChangePanel() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(createButton("Terminal", nodeAction((n1, n2) -> {
            n1.setTerminal(!n1.isTerminal());
            updateNodes();
        })));
        pane.add(createButton("Select", nodeAction((from, to) -> {
            boolean current = from.isSelected();
            if (current == to.isSelected()) {
                boolean changed = !current;
                network.getPath(from, to).forEach(ch -> ch.setSelected(changed));
                from.setSelected(changed);
                to.setSelected(changed);
            }
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
            Config cfg = Config.getConfig();
            int weight = cfg.<WeightList>getProperty("weights").getWeight();
            cfg.<ChannelFactory>getProperty("channelFactory").createChannel(n1, n2, weight);
        })));
        pane.add(createButton("Unlink", nodeAction(network::removeConnection)));
        add(pane);
    }

    private void addNodePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(terminal);
        terminal.addChangeListener(e -> updateNodes());
        panel.add(fromNode);
        panel.add(toNode);
        panel.add(createButton("Connect", nodeAction(network::createConnection)));
        panel.add(createButton("Disconnect", nodeAction(network::closeConnection)));
        panel.add(createButton("Close all", e -> {
            network.closeAll();
            networkPanel.update();
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
        network.getNodes(terminal.isSelected()).forEach(node -> {
            fromNode.addItem(node);
            toNode.addItem(node);
        });
    }

    private ActionListener nodeAction(BiConsumer<GraphNode, GraphNode> consumer) {
        return e -> {
            GraphNode from = (GraphNode) fromNode.getSelectedItem();
            GraphNode to = (GraphNode) toNode.getSelectedItem();
            consumer.accept(from, to);
            networkPanel.update();
        };
    }
}
