package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode

class SimplexChannel(fromNode: GraphNode, toNode: GraphNode, weight: Int, errors: Double) : AbstractChannel(fromNode, toNode, weight, errors) {

    override val direction: String
        get() = "-->"

    init {
        connect()
    }
}
