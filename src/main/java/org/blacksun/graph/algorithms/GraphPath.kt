package org.blacksun.graph.algorithms

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.node.GraphNode

import java.util.ArrayList
import java.util.Spliterator
import java.util.function.Consumer

class GraphPath @JvmOverloads constructor(private val cycle: Boolean = false, private val links: ArrayList<Channel> = ArrayList()) : Iterable<Channel> {

    val length: Int
        get() = links.size

    val weight: Int
        get() = links.sumBy(Channel::weight)

    val from: GraphNode
        get() = links[0].fromNode

    val to: GraphNode
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
            return "Access to self"
        val base = StringBuilder()
        base.append("GraphPath[length=")
                .append(length)
                .append(", weight=")
                .append(weight)
                .append("]:\n\t")
                .append(links[0].fromNode)
                .append(" --> ")
        return base.toString() + links
                .map(Channel::toNode)
                .joinToString(" --> ")
    }

    fun ofNodes(from: GraphNode, to: GraphNode): Boolean {
        if (from === to && cycle)
            return true
        return if (links.isEmpty()) false else from == from && to == to
    }

    // sending package in datagram mode
    fun close(weight: Int): Boolean {
        var sum = 0
        var result = false
        for (i in links.indices.reversed()) {
            val channel = links[i]
            if (weight > sum) {
                sum += channel.weight
            } else {
                channel.isUsed = false
                result = true
            }
        }
        return result
    }
}
