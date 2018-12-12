package mvc.model.channel

import mvc.model.node.Node

class SimplexChannel(fromNode: Node, toNode: Node, weight: Int, errors: Double) : AbstractChannel(fromNode, toNode, weight, errors) {

    override val direction: String
        get() = "-->"

    init {
        connect()
    }
}
