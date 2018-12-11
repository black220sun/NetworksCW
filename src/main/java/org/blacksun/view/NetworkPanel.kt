package org.blacksun.view

import org.blacksun.network.Network
import org.blacksun.utils.Config

import javax.swing.*
import java.awt.*

class NetworkPanel(val network: Network) : JScrollPane() {
    private val render: NetworkRender = NetworkRender(network)

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
