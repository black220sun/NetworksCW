package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode

class HalfDuplexChannelFactory : ChannelFactory {
    override fun createChannel(from: GraphNode, to: GraphNode, weight: Int, errors: Double): Channel {
        return HalfDuplexChannel(from, to, weight, errors)
    }

    override fun toString(): String {
        return "Half-duplex"
    }
}
