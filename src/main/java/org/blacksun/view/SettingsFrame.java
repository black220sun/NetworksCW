package org.blacksun.view;

import guru.nidi.graphviz.attribute.Color;

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
        createCheckBox("Resize graph?", "resize");
        createScroll("Graph width", "graphW", 800, 10_000);
        createScroll("Graph height", "graphH", 800, 10_000);

        createScroll("Min ticks for test", "ticks", 100, 10_000);
        createScroll("Average message size", "message", 256, 8192, 512);
        createScroll("Package size", "package", 128, 2048, 128);
        createScroll("Message appearance delay", "delay", 1, 1000);
        createScroll("Message appearance amount", "amount", 1, 64);

        createColor("Node color", "node");
        createColor("Channel color", "channel");
        createColor("Selected node color", "selectedN");
        createColor("Selected channel color", "selectedC");
        createColor("Connected channel color", "connected");

        setVisible(true);
        pack();
    }

    private void createColor(String name, String cfgName) {
        add(new JLabel(name));
        JComboBox<Color> comboBox = new JComboBox<>(colors);
        comboBox.setSelectedItem(cfg.getColor(cfgName));
        comboBox.addItemListener(e -> cfg.setProperty(cfgName, comboBox.getSelectedItem()));
        add(comboBox);
    }

    private void createCheckBox(String name, String cfgName) {
        JCheckBox checkBox = new JCheckBox(name);
        checkBox.setSelected(cfg.getBoolean(cfgName));
        checkBox.addChangeListener(e -> cfg.setProperty(cfgName, checkBox.isSelected()));
        add(checkBox);
    }

    private void createScroll(String name, String cfgName, int min, int max) {
        createScroll(name, cfgName, min, max, max / 20);
    }

    private void createScroll(String name, String cfgName, int min, int max, int increment) {
        add(new JLabel(name));
        JSlider slider = new JSlider(min, max);
        slider.setValue(cfg.getInt(cfgName));
        slider.createStandardLabels(increment);
        slider.addChangeListener(e -> cfg.setProperty(cfgName, slider.getValue()));
        add(slider);
    }
}
