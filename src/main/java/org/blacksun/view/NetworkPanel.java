package org.blacksun.view;

import org.blacksun.network.Network;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class NetworkPanel extends JScrollPane {
    private final Network network;
    private final NetworkRender render;

    public NetworkPanel(@NotNull Network network) {
        super();
        this.network = network;
        render = new NetworkRender(network);
        viewport.setView(render);
        Config cfg = Config.getConfig();
        setPreferredSize(new Dimension(cfg.getViewWidth(), cfg.getViewHeight()));
    }

    public void update() {
        render.update();
    }

    public Network getNetwork() {
        return network;
    }
}
