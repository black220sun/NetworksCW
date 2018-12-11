package org.blacksun.graph.node

import org.blacksun.graph.channel.ChannelFactory
import org.blacksun.utils.Config

class SimpleNodeFactory : NamedGraphNodeFactory {
    private val cfg = Config.config

    constructor() : super()

    constructor(name: String) : super(name)

    constructor(name: String, factory: ChannelFactory) : super(name, factory)

    override fun createNode(): GraphNode {
        var counter = cfg.getInt("counter")
        val node = SimpleNode(name + counter, cfg.getProperty("channelFactory"))
        cfg.setProperty("counter", ++counter)
        return node
    }
}
