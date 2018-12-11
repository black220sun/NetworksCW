package org.blacksun.view;

import guru.nidi.graphviz.attribute.Color;
import org.blacksun.graph.channel.ChannelFactory;
import org.blacksun.graph.channel.DuplexChannelFactory;
import org.blacksun.graph.channel.HalfDuplexChannelFactory;
import org.blacksun.graph.channel.SimplexChannelFactory;
import org.blacksun.utils.Config;

import javax.swing.*;

public class SettingsFrame extends JFrame {
    private final Config cfg = Config.getConfig();
    private final Color[] colors = new Color[] {
            Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.BROWN,
            Color.CYAN, Color.MAGENTA, Color.GRAY, Color.VIOLET, Color.PURPLE, Color.ORANGE
    };
    private final String[] colorNames = new String[] {
            "BLACK", "BLUE", "RED", "GREEN", "YELLOW", "BROWN",
            "CYAN", "MAGENTA", "GRAY", "VIOLET", "PURPLE", "ORANGE"
    };

    public SettingsFrame(){
        super("Settings");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setContentPane(panel);
        createScroll("View width", "viewW", 400, 2000);
        createScroll("View height","viewH", 400, 2000);
        createResize();
        createScroll("Graph width", "graphW", 800, 10_000);
        createScroll("Graph height", "graphH", 800, 10_000);

        createScroll("Min ticks for test", "ticks", 100, 10_000);
        createScroll("Average message size", "message", 256, 8192, 512);
        createScroll("Package size", "package", 128, 2048, 128);
        createScroll("Message appearance delay", "delay", 1, 1000);
        createScroll("Message appearance amount", "amount", 1, 64);

        createChannel();

        createColor("Node color", "node");
        createColor("Channel color", "channel");
        createColor("Selected node color", "selectedN");
        createColor("Selected channel color", "selectedC");
        createColor("Connected channel color", "connected");

        setVisible(true);
        pack();
    }

    private void createChannel() {
        add(new JLabel("Default channel"));
        JComboBox<ChannelFactory> comboBox = new JComboBox<>(new ChannelFactory[] {
                new SimplexChannelFactory(), new HalfDuplexChannelFactory(), new DuplexChannelFactory()
        });
        comboBox.setSelectedItem(cfg.<ChannelFactory>getProperty("channelFactory"));
        comboBox.addItemListener(e -> cfg.setProperty("channelFactory", comboBox.getSelectedItem()));
        add(comboBox);
    }

    private void createColor(String name, String cfgName) {
        add(new JLabel(name));
        JComboBox<Color> comboBox = new JComboBox<>(colors);
        comboBox.setSelectedItem(cfg.getColor(cfgName));
        comboBox.addItemListener(e -> cfg.setProperty(cfgName, comboBox.getSelectedItem()));
        add(comboBox);
    }

    private void createResize() {
        JCheckBox checkBox = new JCheckBox("Resize graph?");
        checkBox.setSelected(cfg.getBoolean("resize"));
        checkBox.addChangeListener(e -> cfg.setProperty("resize", checkBox.isSelected()));
        add(checkBox);
    }

    private void createScroll(String name, String cfgName, int min, int max) {
        createScroll(name, cfgName, min, max, max / 20);
    }

    private void createScroll(String name, String cfgName, int min, int max, int increment) {
        JLabel label = new JLabel(name);
        add(label);
        JSlider slider = new JSlider(min, max);
        slider.createStandardLabels(increment);
        slider.addChangeListener(e -> {
            int value = slider.getValue();
            cfg.setProperty(cfgName, value);
            label.setText(name + " (" + value + ")");
        });
        slider.setValue(cfg.getInt(cfgName));
        add(slider);
    }
}
