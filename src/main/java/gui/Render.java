package gui;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import network.Network;
import utils.PropertiesHandler;

import javax.swing.*;
import java.awt.*;

class Render extends JPanel {
    private Network network;
    private Image networkImage;

    Render(Network network) {
        super();
        this.network = network;
        update();
    }

    void update() {
        networkImage = Graphviz.fromGraph(network.toGraph())
                .render(Format.SVG)
                .toImage();
        PropertiesHandler cfg = PropertiesHandler.getProps();
        if (cfg.getBoolean("resize")) {
            networkImage = networkImage.getScaledInstance(cfg.getInt("graphW"),
                    cfg.getInt("graphH"), Image.SCALE_SMOOTH);
        }
        setPreferredSize(new Dimension(networkImage.getWidth(this),
                networkImage.getHeight(this)));
        repaint();
        revalidate();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(networkImage, 0, 0, this);
    }
}