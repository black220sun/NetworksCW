package mvc.model.algorithms

import mvc.model.channel.Channel
import mvc.model.node.Node

interface AlgorithmFactory {
    fun getAlgorithm(nodes: List<Node>,
                     links: List<Channel>): Algorithm
}
