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

    public ToolbarPanel(NetworkPanel panel) {
        super();
        this.panel = panel;
        network = panel.getNetwork();
        //setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        GraphNode[] nodes = network.getNodes().toArray(new GraphNode[]{});
        fromNode = new JComboBox<>(nodes);
        toNode = new JComboBox<>(nodes);
        add(fromNode);
        add(toNode);
        addConnectionButton();
        addDisconnectionButton();
    }

    private ActionListener action(BiConsumer<GraphNode, GraphNode> consumer) {
        return e -> {
            GraphNode from = (GraphNode) fromNode.getSelectedItem();
            GraphNode to = (GraphNode) toNode.getSelectedItem();
            consumer.accept(from, to);
            panel.update();
        };
    }

    private void addDisconnectionButton() {
        JButton button = new JButton("Disconnect");
        button.addActionListener(action(network::closeConnection));
        add(button);
    }

    private void addConnectionButton() {
        JButton button = new JButton("Connect");
        button.addActionListener(action(network::createConnection));
        add(button);
    }
}
