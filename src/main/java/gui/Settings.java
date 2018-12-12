package gui;

import graph.edge.ChannelFactory;
import graph.edge.DuplexChannelFactory;
import graph.edge.HalfDuplexChannelFactory;
import graph.edge.SimplexChannelFactory;
import utils.PropertiesHandler;

import javax.swing.*;

public class Settings extends JFrame {
    private final PropertiesHandler cfg = PropertiesHandler.getProps();

    public Settings(){
        super("Налаштування");
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        setContentPane(panel);

        createScroll("Ширина графа", "graphW", 800, 10_000);
        createScroll("Висота графа", "graphH", 800, 10_000);

        createScroll("час генерації по-ння", "ticks", 100, 10_000);
        createScroll("Середній розмір по-ння", "message", 256, 8192, 512);
        createScroll("Розмір пакету", "package", 128, 2048, 128);
        createScroll("затримка між по-нням", "delay", 1, 1000);
        createScroll("Кількість одночасних по-нь", "amount", 1, 64);

        createChannel();

        setVisible(true);
        pack();
    }

    private void createChannel() {
        add(new JLabel("Тип зв'язка"));
        JComboBox<ChannelFactory> comboBox = new JComboBox<>(new ChannelFactory[] {
                new SimplexChannelFactory(), new HalfDuplexChannelFactory(), new DuplexChannelFactory()
        });
        comboBox.setSelectedItem(cfg.<ChannelFactory>getProperty("channelFactory"));
        comboBox.addItemListener(e -> cfg.setProperty("channelFactory", comboBox.getSelectedItem()));
        add(comboBox);
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
            label.setText(name + "[value=" + value + "]");
        });
        slider.setValue(cfg.getInt(cfgName));
        add(slider);
    }
}
