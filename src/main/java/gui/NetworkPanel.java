package gui;

import network.Network;
import utils.PropertiesHandler;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class NetworkPanel extends JScrollPane {
    private final Network network;
    private final Render render;

    public NetworkPanel(@NotNull Network network) {
        super();
        this.network = network;
        render = new Render(network);
        viewport.setView(render);
        PropertiesHandler cfg = PropertiesHandler.getProps();
        setPreferredSize(new Dimension(cfg.getInt("viewW"),
                cfg.getInt("viewH")));
    }

    public void update() {
        render.update();
        revalidate();
    }

    public Network getNetwork() {
        return network;
    }
}
