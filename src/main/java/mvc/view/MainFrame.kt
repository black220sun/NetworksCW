package mvc.view

import mvc.controller.Network
import common.Config

import javax.swing.*
import java.awt.*

class MainFrame(title: String, network: Network) : JFrame(title) {
    constructor(network: Network) : this("Комп'ютерні мережі. Курсовий проект", network)

    init {
        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        val cfg = Config.config
        preferredSize = Dimension(cfg.getInt("frameW"),
                cfg.getInt("frameH"))
        val pane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT)
        val networkPanel = NetworkPanel(network)
        pane.leftComponent = networkPanel
        val panel = JTabbedPane()
        val infoArea = JTextArea()
        cfg.setProperty("info", infoArea)
        cfg.setProperty("tab", panel)
        val toolbar = JPanel()
        toolbar.add(Toolbar(networkPanel))
        panel.addTab("Інструменти", toolbar)
        panel.addTab("Налаштування", SettingsPanel(networkPanel))
        panel.addTab("Інформація", JScrollPane(infoArea))
        pane.rightComponent = panel
        pane.isOneTouchExpandable = true
        contentPane = pane
        isVisible = true
        pack()
    }
}
