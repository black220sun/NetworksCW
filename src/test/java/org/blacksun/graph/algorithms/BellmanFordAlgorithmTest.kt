package org.blacksun.graph.algorithms

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.channel.DuplexChannelFactory
import org.blacksun.graph.node.GraphNode
import org.blacksun.graph.node.NamedGraphNodeFactory
import org.junit.BeforeClass
import org.junit.Test

import java.util.ArrayList

import org.junit.Assert.*

class BellmanFordAlgorithmTest {

    private fun node(index: Int): GraphNode {
        return nodes[index]
    }

    @Test
    fun isAccessible() {
        assertTrue(algorithm.isAccessible(node(0), node(7)))
        assertTrue(algorithm.isAccessible(node(1), node(5)))

        assertTrue(algorithm.isAccessible(node(8), node(8)))

        assertFalse(algorithm.isAccessible(node(0), node(8)))
        assertFalse(algorithm.isAccessible(node(5), node(8)))
    }

    @Test
    fun getMinDistance() {
        assertEquals(7, algorithm.getMinDistance(node(0), node(5)))
        assertEquals(0, algorithm.getMinDistance(node(8), node(8)))
        assertEquals(4, algorithm.getMinDistance(node(0), node(2)))
        assertEquals(6, algorithm.getMinDistance(node(2), node(7)))
        assertEquals(INFINITY, algorithm.getMinDistance(node(0), node(8)))
        assertEquals(INFINITY, algorithm.getMinDistance(node(8), node(2)))
    }

    @Test
    fun getDistances() {
        val map8 = algorithm.getDistances(node(8))
        assertEquals(0, map8[node(8)])
        for (i in 0..7) {
            assertEquals(INFINITY, map8[node(i)])
        }

        val map0 = algorithm.getDistances(node(0))
        assertEquals(0, map0[node(0)])
        assertEquals(3, map0[node(1)])
        assertEquals(4, map0[node(2)])
        assertEquals(7, map0[node(3)])
        assertEquals(5, map0[node(4)])
        assertEquals(7, map0[node(5)])
        assertEquals(6, map0[node(6)])
        assertEquals(8, map0[node(7)])
        assertEquals(INFINITY, map0[node(8)])
    }

    @Test
    fun getPath() {
        assertFalse(algorithm.getPath(node(0), node(8)).exists())
        val path = algorithm.getPath(node(0), node(7))
        assertTrue(path.exists())
        assertEquals(3, path.length.toLong())
        assertEquals(8, path.weight.toLong())
        val it = path.iterator()
        val first = it.next()
        assertEquals(node(0), first.fromNode)
        assertEquals(node(1), first.toNode)
        assertEquals(node(3), it.next().toNode)
        assertEquals(node(7), it.next().toNode)
        assertFalse(it.hasNext())
    }

    companion object {
        private lateinit var algorithm: PathFindingAlgorithm
        private val nodes = ArrayList<GraphNode>()

        @JvmStatic
        @BeforeClass
        fun init() {
            val factory = NamedGraphNodeFactory("Node", DuplexChannelFactory())
            for (i in 0..8)
                nodes.add(factory.createNode())
            connect(0, 1, 3)
            connect(0, 4, 5)
            connect(0, 6, 7)
            connect(1, 2, 1)
            connect(1, 3, 4)
            connect(2, 6, 2)
            connect(3, 7, 1)
            connect(4, 5, 2)
            connect(5, 6, 3)
            connect(5, 7, 4)
            algorithm = BFAlgorithmFactory().getAlgorithm(nodes, getLinks(nodes))
        }

        private fun connect(from: Int, to: Int, weight: Int) {
            nodes[from].addConnectedNode(nodes[to], weight)
        }

        private fun getLinks(nodes: ArrayList<GraphNode>): List<Channel> {
            return nodes
                    .flatMap(GraphNode::connections)
                    .distinct()
                    .filter { !it.isUsed }
        }

        private val INFINITY: Int
            get() = Int.MAX_VALUE
    }
}
