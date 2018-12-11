package org.blacksun.network

import org.blacksun.graph.node.NamedGraphNodeFactory
import org.blacksun.utils.WeightList
import org.junit.Before
import org.junit.Test

import java.util.stream.DoubleStream

import org.junit.Assert.*

class SimpleTopologyTest {

    @Before
    fun init() {
        topology = SimpleTopology(NODES, ORDER, 1, WeightList(1), NamedGraphNodeFactory())
    }

    @Test
    fun createNetwork() {
        assertEquals(NODES.toLong(), topology.createNetwork().size.toLong())
    }

    @Test
    fun correctOrder() {
        // TODO(skip max and min values)
        val average = DoubleStream.generate { Network(topology).avgOrder }
                .limit(300)
                .average().orElse(0.0)

        assertTrue(average > ORDER - 0.4)
        assertTrue(average < ORDER + 0.6)
    }

    companion object {
        private lateinit var topology: Topology
        private const val NODES = 50
        private const val ORDER = 5.5
    }
}