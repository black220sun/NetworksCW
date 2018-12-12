package mvc.model.node

import mvc.model.channel.ChannelFactory
import common.Config

open class NamedNodeFactory @JvmOverloads constructor(protected val name: String = "Node", protected val factory: ChannelFactory = Config.config.getProperty("channelFactory")) : NodeFactory {
    protected var nodesCounter: Int = 0

    init {
        nodesCounter = 0
    }

    override fun createNode(): Node {
        return NamedNode(name + nodesCounter++, factory)
    }
}
