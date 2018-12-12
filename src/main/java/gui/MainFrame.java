package gui;

import network.Network;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(@NotNull Network network) {
        this("NetworkCW", network);
    }

    public MainFrame(String title, @NotNull Network network) {
        super(title);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        PropertiesHandler cfg = PropertiesHandler.getProps();
        setPreferredSize(new Dimension(cfg.getInt("frameW"),
                cfg.getInt("frameH")));
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        NetworkPanel networkPanel = new NetworkPanel(network);
        pane.setLeftComponent(networkPanel);
        JPanel panel = new JPanel();
        panel.add(new Toolbar(networkPanel));
        pane.setRightComponent(panel);
        pane.setOneTouchExpandable(true);
        setContentPane(pane);
        setVisible(true);
        pack();
    }
}
