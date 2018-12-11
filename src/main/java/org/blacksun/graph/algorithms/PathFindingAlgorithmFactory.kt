package org.blacksun.graph.algorithms

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.node.GraphNode

interface PathFindingAlgorithmFactory {
    fun getAlgorithm(nodes: List<GraphNode>,
                     links: List<Channel>): PathFindingAlgorithm
}
