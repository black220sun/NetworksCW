package org.blacksun.graph.algorithms

import org.blacksun.graph.node.GraphNode

import java.util.HashMap

interface PathFindingAlgorithm {
    fun isAccessible(from: GraphNode, to: GraphNode): Boolean
    fun getMinDistance(from: GraphNode, to: GraphNode): Int
    fun getDistances(node: GraphNode): HashMap<GraphNode, Int>
    fun getPath(from: GraphNode, to: GraphNode): GraphPath

    companion object {
        const val INFINITY = Integer.MAX_VALUE
    }
}
