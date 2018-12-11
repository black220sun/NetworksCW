package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode

class DuplexChannel : AbstractChannel {
    private var pair: DuplexChannel

    override val direction = "<==>"

    private constructor(channel: DuplexChannel) : super(channel.toNode, channel.fromNode, channel.weight, channel.errors) {
        pair = channel
    }

    constructor(fromNode: GraphNode, toNode: GraphNode, weight: Int, errors: Double) : super(fromNode, toNode, weight, errors) {
        pair = DuplexChannel(this)
        connect()
    }

    override fun connect() {
        super.connect()
        toNode.addConnection(pair)
    }

    override fun remove() {
        super.remove()
        toNode.removeConnection(pair)
    }
}
