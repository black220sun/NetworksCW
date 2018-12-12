package mvc.model.channel

import mvc.model.node.Node

class DuplexChannelFactory : ChannelFactory {
    override fun createChannel(from: Node, to: Node, weight: Int, errors: Double): Channel {
        return DuplexChannel(from, to, weight, errors)
    }

    override fun toString(): String {
        return "Дуплексний канал"
    }
}
