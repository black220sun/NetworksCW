package mvc.model.channel

import mvc.model.node.Node

class DuplexChannel : AbstractChannel {
    private var pair: DuplexChannel

    override val direction = "<==>"

    private constructor(channel: DuplexChannel) : super(channel.toNode, channel.fromNode, channel.weight, channel.errors) {
        pair = channel
    }

    constructor(fromNode: Node, toNode: Node, weight: Int, errors: Double) : super(fromNode, toNode, weight, errors) {
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
