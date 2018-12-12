package mvc.model.channel

import mvc.model.node.Node

interface ChannelFactory {
    fun createChannel(from: Node, to: Node, weight: Int): Channel {
        return createChannel(from, to, weight, Channel.ERRORS_AMOUNT)
    }

    fun createChannel(from: Node, to: Node, weight: Int, errors: Double): Channel
}
