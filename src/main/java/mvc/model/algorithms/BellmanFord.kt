package mvc.model.algorithms

import mvc.model.channel.Channel
import mvc.model.node.Node
import common.Pair
import java.util.*

class BellmanFord(private val nodes: List<Node>, private val links: List<Channel>) : Algorithm {
    private var map: HashMap<Pair<Node, Int>, Int> = HashMap()
    private var pathMap: HashMap<Pair<Node, Int>, Node>? = null
    private var last: Node? = null

    init {
        last = null
    }

    private fun computePaths(from: Node) {
        if (from == last)
        // already computed, return cached
            return
        last = from
        map.clear()
        pathMap = HashMap()
        val size = nodes.size
        for (node in nodes) {
            for (i in 0 until size) {
                map[Pair(node, i)] = Algorithm.INFINITY
            }
        }
        map[Pair(from, 0)] = 0
        for (i in 1 until size) {
            for (channel in links) {
                val vi = Pair(channel.toNode, i)
                val fromNode = channel.fromNode
                val ui = Pair(fromNode, i - 1)
                val oldWeight = map[ui]
                if (oldWeight == Algorithm.INFINITY)
                    continue
                val newWeight = oldWeight!! + channel.weight
                if (map[vi]!! > newWeight) {
                    map[vi] = newWeight
                    pathMap!![vi] = fromNode
                }
            }
        }
    }

    override fun isAccessible(from: Node, to: Node): Boolean {
        if (!nodes.contains(from) || !nodes.contains(to))
            return false
        computePaths(from)
        return map.keys
                .stream()
                .anyMatch { pair -> pair.first == to && map[pair] != Algorithm.INFINITY }
    }

    override fun getMinDistance(from: Node, to: Node): Int {
        if (!nodes.contains(from) || !nodes.contains(to))
            return Algorithm.INFINITY
        computePaths(from)
        return map[minKey(to)]!!
    }

    private fun minKey(to: Node): Pair<Node, Int> {
        return map.keys
                .stream()
                .filter { pair -> pair.first == to }
                .min(Comparator.comparingInt { map[it]!! })
                .orElseThrow { RuntimeException("Empty mvc.model detected") }
    }

    override fun getDistances(node: Node): HashMap<Node, Int> {
        val result = HashMap<Node, Int>()
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

    override fun getPath(from: Node, to: Node): GraphPath {
        var to = to
        val path = GraphPath()
        if (!nodes.contains(from) || !nodes.contains(to))
            return path
        computePaths(from)
        val size = minKey(to).second
        val nodes = arrayOfNulls<Node>(size + 1)
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
