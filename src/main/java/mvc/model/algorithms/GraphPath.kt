package mvc.model.algorithms

import mvc.model.channel.Channel
import mvc.model.node.Node

import java.util.ArrayList
import java.util.Spliterator
import java.util.function.Consumer

class GraphPath @JvmOverloads constructor(private val cycle: Boolean = false, private val links: ArrayList<Channel> = ArrayList()) : Iterable<Channel> {

    val length: Int
        get() = links.size

    val weight: Int
        get() = links.sumBy(Channel::weight)

    val from: Node
        get() = links[0].fromNode

    val to: Node
        get() = links[links.size - 1].toNode

    fun add(channel: Channel): GraphPath {
        links.add(channel)
        return this
    }

    fun clear(): GraphPath {
        links.clear()
        return this
    }

    fun remove(): GraphPath {
        if (!links.isEmpty())
            links.removeAt(links.size - 1)
        return this
    }

    fun exists(): Boolean {
        return cycle || !links.isEmpty()
    }

    override fun iterator(): Iterator<Channel> {
        return links.iterator()
    }

    override fun forEach(action: Consumer<in Channel>) {
        links.forEach(action)
    }

    override fun spliterator(): Spliterator<Channel> {
        return links.spliterator()
    }

    override fun toString(): String {
        if (!exists())
            return ""
        if (links.isEmpty())
            return "Цикл"
        val base = StringBuilder()
        base.append("Шлях (")
                .append(to)
                .append("): транзитних ділянок ")
                .append(length)
                .append(", загальна вага ")
                .append(weight)
                .append(" [")
                .append(links[0].fromNode)
                .append(" -> ")
        return base.toString() + links
                .map(Channel::toNode)
                .joinToString(" -> ") + "]"
    }

    fun ofNodes(from: Node, to: Node): Boolean {
        if (from === to && cycle)
            return true
        return if (links.isEmpty()) false else from == from && to == to
    }

    // sending package in datagram mode
    fun close(weight: Int): Boolean {
        var sum = 0
        for (i in links.indices.reversed()) {
            val channel = links[i]
            if (weight > sum) {
                sum += channel.weight
            } else {
                channel.isUsed = false
                return true
            }
        }
        return false
    }
}
