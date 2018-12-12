package mvc.controller

import guru.nidi.graphviz.attribute.Label
import guru.nidi.graphviz.model.Factory
import guru.nidi.graphviz.model.MutableGraph
import guru.nidi.graphviz.model.MutableNode
import mvc.model.algorithms.BFAFactory
import mvc.model.algorithms.GraphPath
import mvc.model.algorithms.AlgorithmFactory
import mvc.model.channel.Channel
import mvc.model.node.Node
import common.ToInfo
import common.Config

import java.util.*

import guru.nidi.graphviz.model.Factory.*
import mvc.model.channel.SimplexChannelFactory
import mvc.model.node.SimpleNode

class Network @JvmOverloads constructor(private val topology: Topology = object : Topology {
    override fun createNetwork(): MutableList<Node> {
        return ArrayList()
    }

    override fun createNode(): Node {
        return SimpleNode("Node", SimplexChannelFactory())
    }
}, var factory: AlgorithmFactory = BFAFactory()) : ToInfo {
    private var nodes: MutableList<Node> = ArrayList()
    private var openConnections: MutableList<GraphPath> = ArrayList()

    val randomNode: Node
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

    fun getNodes(terminal: Boolean = false): List<Node> {
        return if (!terminal)
            nodes
        else
            nodes.filter(Node::isTerminal)
    }

    fun getRandomNode(terminal: Boolean): Node {
        val nodes = getNodes(terminal)
        return nodes[Random().nextInt(nodes.size)]
    }

    @JvmOverloads
    fun getPath(from: Node, to: Node, isUsed: Boolean = false): GraphPath {
        return if (!exists(from, to)) GraphPath() else factory.getAlgorithm(nodes, getChannels(isUsed)).getPath(from, to)
    }

    fun getPaths(from: Node): List<GraphPath> {
        val algorithm = factory.getAlgorithm(nodes, channels)
        return nodes
                .filter {  it != from }
                .map {algorithm.getPath(from, it) }
    }

    private fun getChannels(isUsed: Boolean): List<Channel> {
        return nodes
                .flatMap(Node::connections)
                .distinct()
                .filter { it.isUsed == isUsed }
    }

    fun createConnection(from: Node, to: Node): GraphPath {
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


    fun closeConnection(from: Node, to: Node) {
        openConnections.stream()
                .filter { path -> path.ofNodes(from, to) }
                .findAny()
                .ifPresent { this.closeConnection(it) }
    }

    fun addNode(): Node {
        val node = topology.createNode()
        nodes.add(node)
        return node
    }

    fun addNode(node: Node) {
        if (!exists(node)) {
            nodes.add(node)
        }
    }

    fun removeNode(node: Node) {
        val connections = node.connections.toTypedArray()
        for (ch in connections) {
            ch.remove()
        }
        nodes.remove(node)
    }

    fun addConnection(from: Node, to: Node, weight: Int) {
        if (exists(from, to)) {
            from.addConnectedNode(to, weight)
        }
    }

    fun removeConnection(from: Node, to: Node) {
        if (exists(from, to)) {
            from.removeConnectedNode(to)
        }
    }

    private fun exists(vararg nodes: Node): Boolean {
        return nodes.all { it in nodes }
    }

    override fun toInfo(): String {
        val sb = StringBuilder()
        sb.append("Network[order=")
                .append(avgOrder)
                .append("] {\n")
        nodes.forEach { sb.append(it.toInfo()).append(",\n") }
        sb.append("}")
        return sb.toString()
    }

    fun toGraph(): MutableGraph {
        val cfg = Config.config
        val map = HashMap<Node, MutableNode>()
        val g = mutGraph("mvc/controller")
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
