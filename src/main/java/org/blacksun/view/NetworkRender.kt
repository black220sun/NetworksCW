package org.blacksun.view

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import org.blacksun.network.Network
import org.blacksun.utils.Config

import javax.swing.*
import java.awt.*

internal class NetworkRender(private val network: Network) : JPanel() {
    private var networkImage: Image? = null

    init {
        update()
    }

    fun update() {
        networkImage = Graphviz.fromGraph(network.toGraph())
                .render(Format.SVG)
                .toImage()
        val cfg = Config.config
        if (cfg.getBoolean("resize")) {
            networkImage = networkImage!!.getScaledInstance(cfg.getInt("graphW"),
                    cfg.getInt("graphH"), Image.SCALE_SMOOTH)
        }
        preferredSize = Dimension(networkImage!!.getWidth(this),
                networkImage!!.getHeight(this))
        repaint()
        revalidate()
    }

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        g.drawImage(networkImage, 0, 0, this)
    }
}
