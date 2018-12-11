package org.blacksun.network

import org.blacksun.graph.node.GraphNode

interface Topology {
    fun createNetwork(): MutableList<GraphNode>
    fun createNode(): GraphNode
}
