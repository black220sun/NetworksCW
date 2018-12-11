package org.blacksun.graph.node

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.channel.DuplexChannelFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class NamedGraphNodeTest {

    @Before
    fun init() {
        val factory = DuplexChannelFactory()
        node = NamedGraphNode("Test node", factory)
        node2 = NamedGraphNode("Tmp node", factory)
    }

    @Test
    fun getConnectedNodes() {
        node.addConnectedNode(node2, 3)
        node.addConnectedNode(node2, 2)
        node.addConnectedNode(node2, 5)

        val result = node.connectedNodes

        assertEquals(3, result.size.toLong())
        result.forEach { node -> assertEquals(node2, node) }
    }

    @Test
    fun getConnections() {
        node.addConnectedNode(node2, 3)
        node.addConnectedNode(node2, 2)
        node.addConnectedNode(node2, 5)

        val expected = ArrayList<Int>()
        expected.add(2)
        expected.add(3)
        expected.add(5)
        val it = expected.iterator()

        node.connections
                .sortedBy(Channel::weight)
                .map(Channel::weight)
                .forEach { weight -> assertEquals(it.next(), weight) }
    }

    //TODO(more nodes?)
    @Test
    fun isConnected() {
        assertFalse(node.isConnected(node2))
        assertFalse(node2.isConnected(node))
        assertFalse(node.isConnected(node))

        node.addConnectedNode(node2, 7)

        assertTrue(node.isConnected(node2))
        assertTrue(node2.isConnected(node))
        assertFalse(node.isConnected(node))
    }

    @Test
    fun getOrder() {
        assertEquals(0, node.order.toLong())

        node.addConnectedNode(node2, 1)
        node.addConnectedNode(node2, 1)
        node.addConnectedNode(node2, 1)
        assertEquals(3, node.order.toLong())
        assertEquals(3, node2.order.toLong())

        node.removeConnectedNode(node2)
        assertEquals(2, node.order.toLong())
        assertEquals(2, node2.order.toLong())

        node.removeConnectedNode(node2)
        node.removeConnectedNode(node2)
        assertEquals(0, node.order.toLong())
        assertEquals(0, node2.order.toLong())

        node.removeConnectedNode(node2)
        assertEquals(0, node.order.toLong())
        assertEquals(0, node2.order.toLong())
    }

    @Test
    fun stringRepresentation() {
        assertEquals("Test node [\n]", node.stringRepresentation())
    }

    @Test
    fun stringRepresentation2() {
        node.addConnectedNode(node2, 7)

        val expected = ("Test node [\n\t"
                + node + " <==> " + node2
                + " (weight=7, errors=10.0%)\n]")
        assertEquals(expected, node.stringRepresentation())
    }

    companion object {
        private lateinit var node: GraphNode
        private lateinit var node2: GraphNode
    }
}
