package org.blacksun.view;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import org.blacksun.network.Network;

import javax.swing.*;
import java.awt.*;

class NetworkRender extends JPanel {
    private Network network;
    private Image networkImage;

    NetworkRender(Network network) {
        super();
        this.network = network;
        update();
        setPreferredSize(new Dimension(networkImage.getWidth(this),
                networkImage.getHeight(this)));
    }

    void update() {
        networkImage = Graphviz.fromGraph(network.toGraph())
                .render(Format.SVG)
                .toImage();
        Config cfg = Config.getConfig();
        if (cfg.isResizeGraph()) {
            networkImage = networkImage.getScaledInstance(cfg.getGraphWidth(),
                    cfg.getGraphHeight(), Image.SCALE_SMOOTH);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(networkImage, 0, 0, this);
    }
}
