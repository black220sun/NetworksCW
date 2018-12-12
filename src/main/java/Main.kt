import mvc.model.algorithms.BFAFactory
import mvc.model.node.SimpleNodeFactory
import mvc.controller.Network
import mvc.controller.RegionalTopology
import common.WeightList
import common.Config
import mvc.view.MainFrame

import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        //initLogger()

        /* change options before creating frame */
        // Config cfg = Config.getConfig();
        MainFrame(initNetwork())
    }

    private fun initNetwork(): Network {
        val weights = WeightList(2, 3, 5, 8, 11, 12, 14, 15, 18, 20, 21, 22)
        Config.config.setProperty("weights", weights)
        val nodeFactory = SimpleNodeFactory("Вузол")
        val topology = RegionalTopology(3, 8, 3.5, 3, weights, nodeFactory)
        val factory = BFAFactory()

        return Network(topology, factory)
    }

    private fun initLogger() {
        val handler = FileHandler("summary.log")
        handler.formatter = SimpleFormatter()
        Logger.getGlobal().addHandler(handler)
        Logger.getGlobal().useParentHandlers = false
    }
}
