package mvc.model.node

import mvc.model.channel.Channel
import mvc.model.channel.ChannelFactory

import java.util.ArrayList
import java.util.Objects

open class NamedNode internal constructor(var name: String, private val factory: ChannelFactory) : Node {
    override val connections = ArrayList<Channel>()
    override var isSelected = false
    override var isTerminal = false

    override val connectedNodes: List<Node>
        get() = connections.map { it.toNode }

    override val order: Int
        get() = connections.size

    override fun isConnected(node: Node): Boolean {
        return connections.stream().anyMatch { ch -> ch.toNode == node }
    }

    override fun getConnection(node: Node): Channel {
        return connections.stream()
                .filter { ch -> ch.toNode == node }
                .findAny()
                .orElseThrow { RuntimeException(node.toString() + " is not connected") }
    }

    override fun addConnection(channel: Channel): Channel {
        connections.add(channel)
        return channel
    }

    override fun addConnectedNode(node: Node, weight: Int): Channel {
        return factory.createChannel(this, node, weight)
    }

    override fun removeConnection(channel: Channel) {
        connections.remove(channel)
    }

    override fun removeConnectedNode(node: Node) {
        connections.stream()
                .filter { ch -> ch.toNode == node }
                .findAny()
                .ifPresent { it.remove() }
    }

    override fun toInfo(): String {
        val sb = StringBuilder()
        sb.append(name).append(" [\n")
        connections.forEach { ch ->
            sb.append("\t")
                    .append(ch.toInfo())
                    .append("\n")
        }
        sb.append("]")
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other == null || javaClass != other.javaClass)
            return false
        val that = other as NamedNode?
        return name == that!!.name && toString() == that.toString() // && Objects.equals(connections, that.connections);
    }

    override fun hashCode(): Int {
        return Objects.hash(name, toString()) //, connections);
    }

    override fun toString(): String {
        return "Node[name=$name, order=$order]"
    }
}
