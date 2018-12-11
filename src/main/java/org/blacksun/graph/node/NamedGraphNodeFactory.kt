package org.blacksun.graph.node

import org.blacksun.graph.channel.ChannelFactory
import org.blacksun.utils.Config

open class NamedGraphNodeFactory @JvmOverloads constructor(protected val name: String = "Node", protected val factory: ChannelFactory = Config.config.getProperty("channelFactory")) : GraphNodeFactory {
    protected var nodesCounter: Int = 0

    init {
        nodesCounter = 0
    }

    override fun createNode(): GraphNode {
        return NamedGraphNode(name + nodesCounter++, factory)
    }
}
