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
        panel4();
        infoArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(infoArea);
        jScrollPane.getViewport().setPreferredSize(new Dimension(400, 800));
        add(jScrollPane);
    }

    private void panel4() {
        JPanel panel4 = new JPanel();
        panel4.setLayout(new BoxLayout(panel4, BoxLayout.X_AXIS));
        panel4.add(createButton("Налаштування", e -> new Settings()));
        add(panel4);
    }

    private void panel3() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(createButton("Зв'язати", nodeAction(network::createConnection)));
        panel.add(createButton("Роз'єднання", nodeAction(network::closeConnection)));
        panel.add(createButton("Роз'єднати всі", e -> {
            network.closeAll();
            networkPanel.update();
        }));
        panel.add(createButton("Таблиця", e -> {
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
        pane.add(createButton("Термінал", nodeAction((n1, n2) -> {
            n1.setTerminal(!n1.isTerminal());
            updateNodes();
        })));
        pane.add(createButton("Додати", e -> {
            network.addNode();
            updateNodes();
            networkPanel.update();
        }));
        pane.add(createButton("Видалити", nodeAction((node, n) -> {
            network.removeNode(node);
            updateNodes();
        })));
        pane.add(createButton("Створити канал", nodeAction((n1, n2) -> {
            PropertiesHandler cfg = PropertiesHandler.getProps();
            int weight = cfg.<WeightList>getProperty("weights").getWeight();
            cfg.<ChannelFactory>getProperty("channelFactory").createChannel(n1, n2, weight);
        })));
        pane.add(createButton("Роз'єднати канал", nodeAction(network::removeConnection)));
        add(pane);
    }

    private void panel1() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.add(fromNode);
        panel.add(toNode);
        panel.add(createButton("Видалити все", e -> {
            network.clear();
            updateNodes();
            networkPanel.update();
        }));
        panel.add(createButton("Згенерувати", e -> {
            PropertiesHandler.getProps().setProperty("counter", 0);
            network.generateNetwork();
            updateNodes();
            networkPanel.update();
        }));
        panel.add(createButton("Запустити тести", e -> {
            infoArea.setText("");
            network.closeAll();
            infoArea.setText(TestRunner.getConfigOptions() +
                    "\nВіртуальний канал\n" +
                    new TestRunner(network).runTests(false) +
                    "\nДейтаграмний режим\n" +
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
