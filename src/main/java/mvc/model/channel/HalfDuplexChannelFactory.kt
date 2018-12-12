package mvc.model.channel

import mvc.model.node.Node

class HalfDuplexChannelFactory : ChannelFactory {
    override fun createChannel(from: Node, to: Node, weight: Int, errors: Double): Channel {
        return HalfDuplexChannel(from, to, weight, errors)
    }

    override fun toString(): String {
        return "Напівдуплексний канал"
    }
}
