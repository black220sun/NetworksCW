package org.blacksun.view

import org.blacksun.network.Network
import org.blacksun.utils.Config

import javax.swing.*
import java.awt.*

class MainFrame(title: String, network: Network) : JFrame(title) {
    constructor(network: Network) : this("NetworkCW", network)

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        val cfg = Config.config
        preferredSize = Dimension(cfg.getInt("frameW"),
                cfg.getInt("frameH"))
        val pane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val networkPanel = NetworkPanel(network)
        pane.leftComponent = networkPanel
        val panel = JPanel()
        panel.add(ToolbarPanel(networkPanel))
        pane.rightComponent = panel
        pane.isOneTouchExpandable = true
        contentPane = pane
        isVisible = true
        pack()
    }
}
