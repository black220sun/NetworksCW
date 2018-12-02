package org.blacksun.view;

import org.blacksun.graph.node.GraphNode;
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

    public ToolbarPanel(NetworkPanel panel) {
        super();
        this.panel = panel;
        network = panel.getNetwork();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GraphNode[] nodes = network.getNodes().toArray(new GraphNode[]{});
        fromNode = new JComboBox<>(nodes);
        toNode = new JComboBox<>(nodes);
        text = new JTextField();
        addNodePanel();
        addViewPanel();
        addGraphPanel();
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
        add(panel);
    }

    private void addViewPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(text);
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
