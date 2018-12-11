package org.blacksun.network

import org.blacksun.graph.algorithms.GraphPath
import org.blacksun.graph.node.GraphNode
import org.blacksun.utils.Pair
import org.blacksun.utils.RandomGenerator
import org.blacksun.utils.Config

import java.util.ArrayList
import java.util.logging.Logger

class NetworkSummary(private val network: Network) {
    private val logger: Logger = Logger.getGlobal()
    private val waiting: ArrayList<Pair<Pair<GraphNode, GraphNode>, Int>> = ArrayList()
    private var updated = true
    private var toSend: ArrayList<Pair<GraphPath, Int>> = ArrayList()
    private var time: Int = 0
    private var packagesSent = 0
    private var messagesSent = 0
    private var bytesSent = 0
    private var createdConnections = 0

    private fun prepareMessage(datagram: Boolean) {
        val messageSize = cfg.getInt("message")
        val random = RandomGenerator((messageSize * 1.5).toInt(),
                (messageSize * 0.5).toInt())
        val fromNode = network.getRandomNode(true)
        var toNode: GraphNode
        do {
            toNode = network.getRandomNode(true)
        } while (fromNode == toNode)
        var sending = random.next()
        logger.info("Sending $sending bytes from $fromNode to $toNode")
        if (datagram) {
            val packageSize = cfg.getInt("package")
            while (sending > packageSize) {
                waiting.add(Pair(Pair(fromNode, toNode), packageSize))
                sending -= packageSize
            }
        } else {
            // channel initiation
            bytesSent += cfg.getInt("utility")
            packagesSent++
        }
        waiting.add(Pair(Pair(fromNode, toNode), sending))
        messagesSent++
    }

    private fun tryPrepare() {
        if (updated) {
            val toRemove = ArrayList<Pair<Pair<GraphNode, GraphNode>, Int>>()
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
            updated = false
        }
    }

    private fun computePair(path: GraphPath, messageSize: Int): Pair<GraphPath, Int> {
        val size = cfg.getInt("package")
        val amount = (messageSize + size - 1) / size
        val ticks = amount * path.weight
        logger.info("Created $amount package(s). Time to deliver: $ticks ticks")
        packagesSent += amount
        bytesSent += messageSize
        createdConnections += path.length
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
        val ticks = time.toDouble()
        val msg = messagesSent.toDouble()
        val results = "SUMMARY\n" +
                "Time spent: " + time + " ticks\n" +
                "Messages sent: " + messagesSent + "\n" +
                "Messages sent per tick: " + messagesSent / ticks + "\n" +
                "Ticks per message: " + time / msg + "\n" +
                "Packages sent: " + packagesSent + "\n" +
                "Packages sent per tick: " + packagesSent / ticks + "\n" +
                "Ticks per package: " + ticks / packagesSent + "\n" +
                "Packages sent per message: " + packagesSent / msg + "\n" +
                "Bytes sent: " + bytesSent + "\n" +
                "Bytes sent per tick: " + bytesSent / ticks + "\n" +
                "Bytes sent per message: " + bytesSent / msg + "\n" +
                "Connections created: " + createdConnections + "\n" +
                "Connections created per tick: " + createdConnections / ticks + "\n" +
                "Connections created per message: " + createdConnections / msg
        logger.info(results)
        return results
    }

    private fun iteration(datagram: Boolean) {
        tryPrepare()
        send(datagram)
        time++
    }

    fun runTests(datagram: Boolean): String {
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
        return summary()
    }

    companion object {
        private val cfg = Config.config

        val configOptions: String
            get() = "ENVIRONMENT\n" +
                    "Default channel type: " + cfg.getProperty("channelFactory") + "\n" +
                    "Base time: " + cfg.getInt("ticks") + "\n" +
                    "Average message size: " + cfg.getInt("message") + "\n" +
                    "Package size: " + cfg.getInt("package") + "\n" +
                    "Message appearance delay: " + cfg.getInt("delay") + "\n" +
                    "Message appearance amount: " + cfg.getInt("amount")
    }
}
