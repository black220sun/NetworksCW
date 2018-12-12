package mvc.model.algorithms

import mvc.model.node.Node

import java.util.HashMap

interface Algorithm {
    fun isAccessible(from: Node, to: Node): Boolean
    fun getMinDistance(from: Node, to: Node): Int
    fun getDistances(node: Node): HashMap<Node, Int>
    fun getPath(from: Node, to: Node): GraphPath

    companion object {
        const val INFINITY = Integer.MAX_VALUE
    }
}
