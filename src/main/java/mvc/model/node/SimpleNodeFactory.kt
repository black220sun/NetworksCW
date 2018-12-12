package mvc.model.node

import mvc.model.channel.ChannelFactory
import common.Config

class SimpleNodeFactory : NamedNodeFactory {
    private val cfg = Config.config

    constructor() : super()

    constructor(name: String) : super(name)

    constructor(name: String, factory: ChannelFactory) : super(name, factory)

    override fun createNode(): Node {
        var counter = cfg.getInt("counter")
        val node = SimpleNode(name + counter, cfg.getProperty("channelFactory"))
        cfg.setProperty("counter", ++counter)
        return node
    }
}
