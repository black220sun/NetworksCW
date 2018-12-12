package mvc.controller

import mvc.model.node.Node
import mvc.model.node.NodeFactory
import common.Randomer
import common.WeightList

import java.util.ArrayList

class CommonTopology(private val amount: Int, order: Double, private val terminal: Int,
                     private val weights: WeightList, private val factory: NodeFactory) : Topology {
    private val order: Int
    private val lower: Int

    init {
        if (amount < 0)
            throw IllegalArgumentException("Amount must be non-negative")
        if (order <= 0)
            throw IllegalArgumentException("Order must be greater than 0")
        this.order = (order * 1.75).toInt()
        lower = if (this.order > 1) 1 else 0
    }

    override fun createNetwork(): MutableList<Node> {
        val nodes = ArrayList<Node>()
        // create all nodes
        for (i in 0 until amount) {
            val node = factory.createNode()
            if (i % terminal == 0) {
                node.isTerminal = true
            }
            nodes.add(node)
        }
        val orderGen = Randomer(order, lower)
        val indexGen = Randomer(amount)
        var node: Node
        var other: Node
        var thisOrder: Int
        var currentOrder: Int
        // link nodes
        for (index in 0 until amount) {
            // TODO (preserve correct order for already processed nodes)
            // current implementation creates slightly more links (see test class)
            node = nodes[index]
            thisOrder = orderGen.next()
            currentOrder = node.connectedNodes.size
            while (currentOrder < thisOrder) {
                other = nodes[indexGen.nextExcept(index)]
                if (!node.isConnected(other)) {
                    node.addConnectedNode(other, weights.weight)
                    ++currentOrder
                }
            }
        }
        return nodes
    }

    override fun createNode(): Node {
        return factory.createNode()
    }
}
