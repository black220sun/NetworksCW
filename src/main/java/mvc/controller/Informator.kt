package mvc.controller

import mvc.model.algorithms.GraphPath
import mvc.model.node.Node
import common.Pair
import common.Randomer
import common.Config
import java.util.*

import java.util.logging.Logger

class Informator(private val network: Network) {
    private data class Result(
            var time: Int = 0,
            var packagesSent: Int = 0,
            var utilitySent: Int = 0,
            var messagesSent: Int = 0,
            var createdConnections: Int = 0)
    private val logger: Logger = Logger.getGlobal()
    private val waiting: ArrayList<Pair<Pair<Node, Node>, Int>> = ArrayList()
    private var updated = true
    private var toSend: ArrayList<Pair<GraphPath, Int>> = ArrayList()
    private val results = Stack<Result>()

    private fun prepareMessage(datagram: Boolean) {
        val messageSize = cfg.getInt("message")
        val random = Randomer((messageSize * 1.5).toInt(),
                (messageSize * 0.5).toInt())
        val fromNode = network.getRandomNode(true)
        var toNode: Node
        do {
            toNode = network.getRandomNode(true)
        } while (fromNode == toNode)
        var sending = random.next()
        logger.info("Sending $sending bytes from $fromNode to $toNode")
        val peek = results.peek()
        if (datagram) {
            val packageSize = cfg.getInt("package")
            while (sending > packageSize) {
                waiting.add(Pair(Pair(fromNode, toNode), packageSize))
                sending -= packageSize
            }
        } else {
            // channel initiation/confirm/close
            peek.utilitySent += 3
        }
        waiting.add(Pair(Pair(fromNode, toNode), sending))
        peek.messagesSent++
    }

    private fun tryPrepare() {
        if (updated) {
            val toRemove = ArrayList<Pair<Pair<Node, Node>, Int>>()
            waiting.forEach { entry ->
                val pair = entry.first
                val size = entry.second
                val path = network.createConnection(pair.first, pair.second)
                if (path.exists()) {
                    logger.info("Created connection:\n\t$path")
                    toRemove.add(entry)
                    toSend.add(computePair(path, size))
                }
            }
            toRemove.forEach { waiting.remove(it) }
            //updated = false
        }
    }

    private fun computePair(path: GraphPath, messageSize: Int): Pair<GraphPath, Int> {
        val size = cfg.getInt("package")
        val amount = (messageSize + size - 1) / size
        val ticks = amount * path.weight
        logger.info("Created $amount package(s). Time to deliver: $ticks ticks")
        val peek = results.peek()
        peek.packagesSent += amount
        peek.utilitySent += amount
        peek.createdConnections += path.length
        return Pair(path, ticks)
    }

    private fun send(datagram: Boolean) {
        val newToSend = ArrayList<Pair<GraphPath, Int>>()
        toSend.forEach { pair ->
            val newLeft = pair.second - 1
            val path = pair.first
            if (datagram) {
                updated = path.close(newLeft)
            }
            if (newLeft > 0) {
                newToSend.add(Pair(path, newLeft))
            } else {
                val what = if (datagram) "Package" else "Message"
                logger.info(what + " sent from " + path.from + " to " +
                        path.to + ". Closing connection")
                updated = true
                if (!datagram) {
                    network.closeConnection(path)
                }
            }
        }
        toSend = newToSend
    }

    private fun summary(): String {
        val resDG = results.pop()
        val resVC = results.pop()
        val packageSize = cfg.getInt("package")
        val util = cfg.getInt("utility")
        val results =
                "                      \t\tBірт. канал\tДатаграмний режим\n" +
                "Час роботи (такти):   \t" + resVC.time + "\t" + resDG.time + "\n" +
                "Повідомлень:          \t" + resVC.messagesSent + "\t" + resDG.messagesSent + "\n" +
                "Інформаційних пакетів:\t" + resVC.packagesSent + "\t" + resDG.packagesSent + "\n" +
                "Службових пакетів:    \t" + resVC.utilitySent + "\t" + resDG.utilitySent + "\n" +
                "Байтів даних:         \t" + (resVC.packagesSent * packageSize) +
                        "\t" + (resDG.packagesSent * packageSize) + "\n" +
                "Службових байтів:     \t" + (resVC.utilitySent * util) +
                        "\t" + (resDG.utilitySent * util) + "\n"
        logger.info(results)
        return results
    }

    private fun iteration(datagram: Boolean) {
        tryPrepare()
        send(datagram)
        results.peek().time++
    }

    fun runTests(): String {
        runTests(false)
        runTests(true)
        return summary()
    }

    fun runTests(datagram: Boolean) {
        results.add(Result())
        val ticks = cfg.getInt("ticks")
        val delay = cfg.getInt("delay")
        val amount = cfg.getInt("amount")
        for (i in 0 until ticks) {
            if (i % delay == 0) {
                for (j in 0 until amount)
                    prepareMessage(datagram)
            }
            iteration(datagram)
        }
        while (!toSend.isEmpty() || !waiting.isEmpty()) {
            iteration(datagram)
        }
    }

    companion object {
        private val cfg = Config.config

        val configOptions: String
            get() = "Канал: " + cfg.getProperty("channelFactory") + "\n" +
                    "Розмір повідомлення: " + cfg.getInt("message") + " байтів\n" +
                    "Розмір інфор. пакету: " + cfg.getInt("package") + " байтів\n" +
                    "Розмір служб. пакету: " + cfg.getInt("utility") + " байтів\n" +
                    "Частота появи повідомлень: " + cfg.getInt("delay") + " тактів\n"
    }
}
