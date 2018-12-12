package mvc.model.channel

import mvc.model.node.Node

class SimplexChannelFactory : ChannelFactory {
    override fun createChannel(from: Node, to: Node, weight: Int, errors: Double): Channel {
        return SimplexChannel(from, to, weight, errors)
    }

    override fun toString(): String {
        return "Симплексний канал"
    }
}
