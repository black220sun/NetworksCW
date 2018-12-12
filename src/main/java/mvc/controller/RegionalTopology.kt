package mvc.controller

import mvc.model.node.Node
import mvc.model.node.SimpleNodeFactory
import common.Randomer
import common.WeightList

class RegionalTopology(val regions: Int, val nodes: Int, val order: Double,
                       val n: Int, val weights: WeightList, val nodeFactory: SimpleNodeFactory) : Topology {
    override fun createNetwork(): MutableList<Node> {
        val nodesGen = Randomer(nodes + nodes / 2, nodes)
        val regs = (1..regions).map {
            CommonTopology(nodesGen.next(), order, n, weights, nodeFactory).createNetwork()
        }
        val gen = Randomer(nodes)
        val nodes = regs.map { it[gen.next()] }
        nodes.last().addConnectedNode(nodes.first(), weights.weight)
        nodes.subList(1, nodes.lastIndex + 1).forEachIndexed { i, node ->
            node.addConnectedNode(nodes[i], weights.weight)
        }
        return regs.flatten().toMutableList()
    }

    override fun createNode(): Node {
        return nodeFactory.createNode()
    }
}