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
            var messagesSent: Int = 0,
            var bytesSent: Int = 0,
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
            // channel initiation
            peek.bytesSent += cfg.getInt("utility")
            peek.packagesSent++
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
        peek.bytesSent += messageSize
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
        val ticks = resVC.time.toDouble()
        val msg = resVC.messagesSent.toDouble()
        val results =
                "                      Вірт. канал                    Датаграмний режим\n" +
                "Час роботи:         " + resVC.time + "\t" + resDG.time + "\n" +
                "Повідомлень:        " + resVC.messagesSent + "\t" + resDG.messagesSent + "\n" +
                "    (за 1 такт): " + resVC.messagesSent / ticks + "\t" + resDG.messagesSent / ticks + "\n" +
                "Пакетів:            " + resVC.packagesSent + "\t" + resDG.packagesSent + "\n" +
                "    (за 1 такт): " + resVC.packagesSent / ticks + "\t" + resDG.packagesSent / ticks + "\n" +
                "    (на 1 пов.): " + resVC.packagesSent / msg + "\t" + resDG.packagesSent / msg + "\n" +
                "Байтів:             " + resVC.bytesSent + "\t" + resDG.bytesSent + "\n" +
                "    (за 1 такт): " + resVC.bytesSent / ticks + "\t" + resDG.bytesSent / ticks + "\n" +
                "Каналів комутовано: " + resVC.createdConnections + "\t" + resDG.createdConnections
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
                    "Розмір повідомлення: " + cfg.getInt("message") + "\n" +
                    "Розмір пакету: " + cfg.getInt("package") + "\n" +
                    "Частота появи (такти): " + cfg.getInt("delay") + "\n"
    }
}
