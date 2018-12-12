package mvc.model.channel

import mvc.model.node.Node

class HalfDuplexChannel : AbstractChannel {
    private var pair: HalfDuplexChannel

    override var isUsed = false
        set(used) {
            field = used
            if (pair.isUsed != used)
                pair.isUsed = used
        }

    override val direction = "<-->"

    private constructor(channel: HalfDuplexChannel) : super(channel.toNode, channel.fromNode, channel.weight, channel.errors) {
        pair = channel
    }

    constructor(fromNode: Node, toNode: Node, weight: Int, errors: Double) : super(fromNode, toNode, weight, errors) {
        pair = HalfDuplexChannel(this)
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
