package org.blacksun.view;

import org.blacksun.network.Network;
import org.blacksun.utils.Config;
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
