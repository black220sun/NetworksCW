package org.blacksun.network

import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.MutableNode
import org.blacksun.graph.algorithms.BFAlgorithmFactory
import org.blacksun.graph.algorithms.GraphPath
import org.blacksun.graph.algorithms.PathFindingAlgorithmFactory
import org.blacksun.graph.channel.Channel
import org.blacksun.graph.node.GraphNode
import org.blacksun.utils.StringRepresentable
import org.blacksun.utils.Config

import java.util.*

import guru.nidi.graphviz.model.Factory.*
import org.blacksun.graph.channel.SimplexChannelFactory
import org.blacksun.graph.node.SimpleNode

class Network @JvmOverloads constructor(private val topology: Topology = object : Topology {
    override fun createNetwork(): MutableList<GraphNode> {
        return ArrayList()
    }

    override fun createNode(): GraphNode {
        return SimpleNode("Node", SimplexChannelFactory())
    }
}, var factory: PathFindingAlgorithmFactory = BFAlgorithmFactory()) : StringRepresentable {
    private var nodes: MutableList<GraphNode> = ArrayList()
    private var openConnections: MutableList<GraphPath> = ArrayList()

    val randomNode: GraphNode
        get() = nodes[Random().nextInt(nodes.size)]

    private val channels: List<Channel>
        get() = nodes
                .flatMap { node -> node.connections }
                .distinct()

    // TODO(cache?)
    internal val avgOrder: Double
        get() {
            if (nodes.isEmpty())
                return .0
            var sum = .0
            for (node in nodes) {
                sum += node.order.toDouble()
            }
            return sum / nodes.size
        }

    init {
        generateNetwork()
    }

    fun generateNetwork() {
        nodes = topology.createNetwork()
        openConnections = ArrayList()
    }

    fun getNodes(terminal: Boolean): List<GraphNode>? {
        return if (!terminal)
            nodes
        else
            nodes.filter(GraphNode::isTerminal)
    }

    fun getRandomNode(terminal: Boolean): GraphNode {
        val nodes = getNodes(terminal)
        return nodes!![Random().nextInt(nodes.size)]
    }

    @JvmOverloads
    fun getPath(from: GraphNode, to: GraphNode, isUsed: Boolean = false): GraphPath {
        return if (!exists(from, to)) GraphPath() else factory.getAlgorithm(nodes, getChannels(isUsed)).getPath(from, to)
    }

    fun getPaths(from: GraphNode): List<GraphPath> {
        val algorithm = factory.getAlgorithm(nodes, channels)
        return nodes
                .filter {  it != from }
                .map {algorithm.getPath(from, it) }
    }

    private fun getChannels(isUsed: Boolean): List<Channel> {
        return nodes
                .flatMap(GraphNode::connections)
                .distinct()
                .filter { it.isUsed == isUsed }
    }

    fun createConnection(from: GraphNode, to: GraphNode): GraphPath {
        if (from === to)
            return GraphPath()
        val path = getPath(from, to)
        if (path.exists()) {
            path.forEach { it.isUsed = true }
            openConnections.add(path)
        }
        return path
    }

    fun closeConnection(path: GraphPath) {
        if (openConnections.contains(path)) {
            path.forEach { ch -> ch.isUsed = false }
            openConnections.remove(path)
        }
    }


    fun closeConnection(from: GraphNode, to: GraphNode) {
        openConnections.stream()
                .filter { path -> path.ofNodes(from, to) }
                .findAny()
                .ifPresent { this.closeConnection(it) }
    }

    fun addNode(): GraphNode {
        val node = topology.createNode()
        nodes.add(node)
        return node
    }

    fun addNode(node: GraphNode) {
        if (!exists(node)) {
            nodes.add(node)
        }
    }

    fun removeNode(node: GraphNode) {
        val connections = node.connections.toTypedArray()
        for (ch in connections) {
            ch.remove()
        }
        nodes.remove(node)
    }

    fun addConnection(from: GraphNode, to: GraphNode, weight: Int) {
        if (exists(from, to)) {
            from.addConnectedNode(to, weight)
        }
    }

    fun removeConnection(from: GraphNode, to: GraphNode) {
        if (exists(from, to)) {
            from.removeConnectedNode(to)
        }
    }

    private fun exists(vararg nodes: GraphNode): Boolean {
        return nodes.all { it in nodes }
    }

    override fun stringRepresentation(): String {
        val sb = StringBuilder()
        sb.append("Network[order=")
                .append(avgOrder)
                .append("] {\n")
        nodes.forEach { sb.append(it.stringRepresentation()).append(",\n") }
        sb.append("}")
        return sb.toString()
    }

    fun toGraph(): MutableGraph {
        val cfg = Config.config
        val map = HashMap<GraphNode, MutableNode>()
        val g = mutGraph("network")
                .setDirected(true)
        nodes.forEach { node ->
            var color = cfg.getColor("node")
            if (node.isTerminal)
                color = cfg.getColor("terminal")
            if (node.isSelected)
                color = cfg.getColor("selectedN")
            map[node] = mutNode(node.toString()).add(color)
        }
        nodes.forEach { node ->
            val mNode = map[node]!!
            g.add(mNode)
            node.connections.forEach { ch ->
                val toNode = map[ch.toNode]!!
                var color = cfg.getColor("channel")
                if (ch.isUsed)
                    color = cfg.getColor("connected")
                if (ch.isSelected)
                    color = cfg.getColor("selectedC")
                mNode.addLink(Factory.to(toNode).with(Label.markdown(ch.weight.toString()),
                        color))
            }
        }
        return g
    }

    override fun toString(): String {
        return "Network[nodes=" + nodes.size + ", order=" + avgOrder + "]"
    }

    fun closeAll() {
        getChannels(true).forEach { ch -> ch.isUsed = false }
    }

    fun clear() {
        nodes.clear()
        openConnections.clear()
    }
}
