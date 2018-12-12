package mvc.model.node

import mvc.model.channel.ChannelFactory

class SimpleNode internal constructor(name: String, factory: ChannelFactory) : NamedNode(name, factory) {

    override fun toString(): String {
        return name
    }
}
