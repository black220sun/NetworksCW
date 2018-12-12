package mvc.model.algorithms

import mvc.model.channel.Channel
import mvc.model.node.Node

class BFAFactory : AlgorithmFactory {
    override fun getAlgorithm(nodes: List<Node>,
                              links: List<Channel>): Algorithm {
        return BellmanFord(nodes, links)
    }
}
