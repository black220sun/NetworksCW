package org.blacksun.view;

import org.blacksun.network.Network;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame(@NotNull Network network) {
        this("NetworkCW", network);
    }

    public MainFrame(String title, @NotNull Network network) {
        super(title);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Config cfg = Config.getConfig();
        setPreferredSize(new Dimension(cfg.getFrameWidth(), cfg.getFrameHeight()));
        JSplitPane pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        NetworkPanel networkPanel = new NetworkPanel(network);
        pane.setLeftComponent(networkPanel);
        JPanel panel = new JPanel();
        panel.add(new ToolbarPanel(networkPanel));
        pane.setRightComponent(panel);
        pane.setOneTouchExpandable(true);
        setContentPane(pane);
        setVisible(true);
        pack();
    }
}
