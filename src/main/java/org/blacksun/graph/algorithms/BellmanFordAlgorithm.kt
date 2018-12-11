package org.blacksun.graph.algorithms

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.node.GraphNode
import org.blacksun.utils.Pair
import java.util.*

class BellmanFordAlgorithm(private val nodes: List<GraphNode>, private val links: List<Channel>) : PathFindingAlgorithm {
    private var map: HashMap<Pair<GraphNode, Int>, Int> = HashMap()
    private var pathMap: HashMap<Pair<GraphNode, Int>, GraphNode>? = null
    private var last: GraphNode? = null

    init {
        last = null
    }

    private fun computePaths(from: GraphNode) {
        if (from == last)
        // already computed, return cached
            return
        last = from
        map.clear()
        pathMap = HashMap()
        val size = nodes.size
        for (node in nodes) {
            for (i in 0 until size) {
                map[Pair(node, i)] = PathFindingAlgorithm.INFINITY
            }
        }
        map[Pair(from, 0)] = 0
        for (i in 1 until size) {
            for (channel in links) {
                val vi = Pair(channel.toNode, i)
                val fromNode = channel.fromNode
                val ui = Pair(fromNode, i - 1)
                val oldWeight = map[ui]
                if (oldWeight == PathFindingAlgorithm.INFINITY)
                    continue
                val newWeight = oldWeight!! + channel.weight
                if (map[vi]!! > newWeight) {
                    map[vi] = newWeight
                    pathMap!![vi] = fromNode
                }
            }
        }
    }

    override fun isAccessible(from: GraphNode, to: GraphNode): Boolean {
        if (!nodes.contains(from) || !nodes.contains(to))
            return false
        computePaths(from)
        return map.keys
                .stream()
                .anyMatch { pair -> pair.first == to && map[pair] != PathFindingAlgorithm.INFINITY }
    }

    override fun getMinDistance(from: GraphNode, to: GraphNode): Int {
        if (!nodes.contains(from) || !nodes.contains(to))
            return PathFindingAlgorithm.INFINITY
        computePaths(from)
        return map[minKey(to)]!!
    }

    private fun minKey(to: GraphNode): Pair<GraphNode, Int> {
        return map.keys
                .stream()
                .filter { pair -> pair.first == to }
                .min(Comparator.comparingInt { map[it]!! })
                .orElseThrow { RuntimeException("Empty graph detected") }
    }

    override fun getDistances(node: GraphNode): HashMap<GraphNode, Int> {
        val result = HashMap<GraphNode, Int>()
        computePaths(node)
        map.forEach { pair, weight ->
            val key = pair.first
            val old = result[key]
            if (old == null || old > weight) {
                result[key] = weight
            }
        }
        return result
    }

    override fun getPath(from: GraphNode, to: GraphNode): GraphPath {
        var to = to
        val path = GraphPath()
        if (!nodes.contains(from) || !nodes.contains(to))
            return path
        computePaths(from)
        val size = minKey(to).second
        val nodes = arrayOfNulls<GraphNode>(size + 1)
        for (j in size downTo 1) {
            nodes[j] = to
            to = pathMap!![Pair(to, j)] ?: return path
        }
        nodes[0] = from
        for (i in 0 until size) {
            path.add(nodes[i]!!.getConnection(nodes[i + 1]!!))
        }
        return path
    }
}
