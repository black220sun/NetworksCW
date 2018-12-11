package org.blacksun

import org.blacksun.graph.algorithms.BFAlgorithmFactory
import org.blacksun.graph.node.SimpleNodeFactory
import org.blacksun.network.Network
import org.blacksun.network.SimpleTopology
import org.blacksun.utils.WeightList
import org.blacksun.utils.Config
import org.blacksun.view.MainFrame

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        initLogger()

        /* change options before creating frame */
        // Config cfg = Config.getConfig();
        MainFrame(initNetwork())
    }

    private fun initNetwork(): Network {
        val weights = WeightList(2, 3, 6, 8, 9, 11, 12, 15, 17, 19, 21, 24, 27)
        Config.config.setProperty("weights", weights)
        val nodeFactory = SimpleNodeFactory("Node")
        val topology = SimpleTopology(30, 3.0, 2, weights, nodeFactory)
        val factory = BFAlgorithmFactory()

        return Network(topology, factory)
    }

    private fun initLogger() {
        val handler = FileHandler("summary.log")
        handler.formatter = SimpleFormatter()
        Logger.getGlobal().addHandler(handler)
        Logger.getGlobal().useParentHandlers = false
    }
}
