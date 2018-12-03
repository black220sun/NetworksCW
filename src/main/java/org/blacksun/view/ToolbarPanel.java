package org.blacksun.view;

import org.blacksun.graph.channel.Channel;
import org.blacksun.graph.channel.DuplexChannel;
import org.blacksun.graph.channel.HalfDuplexChannel;
import org.blacksun.graph.channel.SimplexChannel;
import org.blacksun.graph.node.GraphNode;
import org.blacksun.graph.node.NamedGraphNode;
import org.blacksun.network.Network;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

public class ToolbarPanel extends JPanel {
    private final NetworkPanel panel;
    private final Network network;
    private final JComboBox<GraphNode> fromNode;
    private final JComboBox<GraphNode> toNode;
    private final JTextField text;
    private final JLabel timeLabel;
    private int time;

    public ToolbarPanel(NetworkPanel panel) {
        super();
        this.panel = panel;
        network = panel.getNetwork();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GraphNode[] nodes = network.getNodes().toArray(new GraphNode[]{});
        fromNode = new JComboBox<>(nodes);
        toNode = new JComboBox<>(nodes);
        text = new JTextField();
        text.setPreferredSize(new Dimension(128, 24));
        addNodePanel();
        add(text);
        addChangePanel();
        addViewPanel();
        addGraphPanel();
        timeLabel = new JLabel("0");
        addTimePanel();
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
        add(panel);
    }

    private void addChangePanel() {
        JPanel pane = new JPanel();
        pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));
        pane.add(createButton("Add", e -> {
            GraphNode node = network.addNode();
            fromNode.addItem(node);
            toNode.addItem(node);
            panel.update();
        }));
        pane.add(createButton("Remove", nodeAction((node, n) -> {
            network.removeNode(node);
            fromNode.removeItem(node);
            toNode.removeItem(node);
        })));
        pane.add(createButton("Rename", nodeAction((node, n) -> {
            String name = text.getText();
            if (!name.isEmpty() && node instanceof NamedGraphNode) {
                ((NamedGraphNode) node).setName(name);
            }
        })));
        pane.add(createButton("Link", nodeAction((n1, n2) -> {
            String name = text.getText().toLowerCase();
            int weight = Config.getConfig().getWeightList().getWeight();
            if (name.startsWith("duplex")) {
                new DuplexChannel(n1, n2, weight, Channel.ERRORS_AMOUNT);
            } else if (name.startsWith("half")) {
                new HalfDuplexChannel(n1, n2, weight, Channel.ERRORS_AMOUNT);
            } else if (name.startsWith("simplex")) {
                new SimplexChannel(n1, n2, weight, Channel.ERRORS_AMOUNT);
            } else { // use default channel
                n1.addConnectedNode(n2, weight);
            }
        })));
        pane.add(createButton("Unlink", nodeAction(network::removeConnection)));
        add(pane);
    }

    private void addGraphPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createResize());
        panel.add(createButton("Graph width", cfgAction(Config::setGraphWidth)));
        panel.add(createButton("Graph height", cfgAction(Config::setGraphHeight)));
        add(panel);
    }

    private void addNodePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(fromNode);
        panel.add(toNode);
        panel.add(createButton("Connect", nodeAction(network::createConnection)));
        panel.add(createButton("Disconnect", nodeAction(network::closeConnection)));
        panel.add(createButton("Select", nodeAction((from, to) -> {
            boolean current = from.isSelected();
            if (current == to.isSelected()) {
                boolean changed = !current;
                network.getPath(from, to).forEach(ch -> ch.setSelected(changed));
                from.setSelected(changed);
                to.setSelected(changed);
            }
        })));
        panel.add(createButton("Close all", e -> {
            network.closeAll();
            this.panel.update();
        }));
        add(panel);
    }

    private void addViewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createButton("Frame width", cfgAction(Config::setFrameWidth)));
        panel.add(createButton("Frame height", cfgAction(Config::setFrameHeight)));
        panel.add(createButton("View width", cfgAction(Config::setViewHeight)));
        panel.add(createButton("View height", cfgAction(Config::setViewWidth)));
        add(panel);
    }

    private Component createResize() {
        Config cfg = Config.getConfig();
        JCheckBox check = new JCheckBox("Resize graph?", cfg.isResizeGraph());
        check.addChangeListener(e -> cfg.setResizeGraph(check.isSelected()));
        return check;
    }

    private Component createButton(String name, ActionListener action) {
        JButton button = new JButton(name);
        button.addActionListener(action);
        return button;
    }

    private ActionListener nodeAction(BiConsumer<GraphNode, GraphNode> consumer) {
        return e -> {
            GraphNode from = (GraphNode) fromNode.getSelectedItem();
            GraphNode to = (GraphNode) toNode.getSelectedItem();
            consumer.accept(from, to);
            panel.update();
        };
    }

    private ActionListener cfgAction(BiConsumer<Config, Integer> consumer) {
        return e -> {
            Config cfg = Config.getConfig();
            int value = Integer.parseInt(text.getText());
            consumer.accept(cfg, value);
            panel.update();
        };
    }
}
