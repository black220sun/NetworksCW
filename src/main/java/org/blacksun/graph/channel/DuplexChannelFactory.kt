package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode

class DuplexChannelFactory : ChannelFactory {
    override fun createChannel(from: GraphNode, to: GraphNode, weight: Int, errors: Double): Channel {
        return DuplexChannel(from, to, weight, errors)
    }

    override fun toString(): String {
        return "Duplex"
    }
}
