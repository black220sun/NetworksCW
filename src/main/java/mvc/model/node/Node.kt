package mvc.model.node

import mvc.model.channel.Channel
import common.Selectable
import common.ToInfo

interface Node : ToInfo, Selectable {
    val connectedNodes: List<Node>
    // returns list or connected nodes with the weight of connection
    val connections: List<Channel>
    var isTerminal: Boolean
    val order: Int
    fun isConnected(node: Node): Boolean
    fun getConnection(node: Node): Channel
    fun addConnectedNode(node: Node, weight: Int): Channel
    fun addConnection(channel: Channel): Channel
    fun removeConnectedNode(node: Node)
    fun removeConnection(channel: Channel)
}
