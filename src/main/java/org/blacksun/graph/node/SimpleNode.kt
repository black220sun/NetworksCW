package org.blacksun.graph.node

import org.blacksun.graph.channel.ChannelFactory

class SimpleNode internal constructor(name: String, factory: ChannelFactory) : NamedGraphNode(name, factory) {

    override fun toString(): String {
        return name
    }
}
