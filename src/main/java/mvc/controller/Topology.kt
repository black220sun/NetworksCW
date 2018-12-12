package mvc.controller

import mvc.model.node.Node

interface Topology {
    fun createNetwork(): MutableList<Node>
    fun createNode(): Node
}
