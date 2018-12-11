package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode

interface ChannelFactory {
    fun createChannel(from: GraphNode, to: GraphNode, weight: Int): Channel {
        return createChannel(from, to, weight, Channel.ERRORS_AMOUNT)
    }

    fun createChannel(from: GraphNode, to: GraphNode, weight: Int, errors: Double): Channel
}
