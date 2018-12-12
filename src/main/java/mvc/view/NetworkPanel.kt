package mvc.view

import mvc.controller.Network
import common.Config

import javax.swing.*
import java.awt.*

class NetworkPanel(val network: Network) : JScrollPane() {
    private val render: GraphImage = GraphImage(network)

    init {
        viewport.view = render
        val cfg = Config.config
        preferredSize = Dimension(cfg.getInt("viewW"),
                cfg.getInt("viewH"))
    }

    fun update() {
        render.update()
        revalidate()
    }
}
