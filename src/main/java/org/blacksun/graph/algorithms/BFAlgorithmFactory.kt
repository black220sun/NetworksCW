package org.blacksun.graph.algorithms

import org.blacksun.graph.channel.Channel
import org.blacksun.graph.node.GraphNode

class BFAlgorithmFactory : PathFindingAlgorithmFactory {
    override fun getAlgorithm(nodes: List<GraphNode>,
                              links: List<Channel>): PathFindingAlgorithm {
        return BellmanFordAlgorithm(nodes, links)
    }
}
