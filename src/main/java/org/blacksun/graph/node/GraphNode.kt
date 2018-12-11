package org.blacksun.graph.node

import org.blacksun.graph.channel.Channel
import org.blacksun.utils.Selectable
import org.blacksun.utils.StringRepresentable

interface GraphNode : StringRepresentable, Selectable {
    val connectedNodes: List<GraphNode>
    // returns list or connected nodes with the weight of connection
    val connections: List<Channel>
    var isTerminal: Boolean
    val order: Int
    fun isConnected(node: GraphNode): Boolean
    fun getConnection(node: GraphNode): Channel
    fun addConnectedNode(node: GraphNode, weight: Int): Channel
    fun addConnection(channel: Channel): Channel
    fun removeConnectedNode(node: GraphNode)
    fun removeConnection(channel: Channel)
}
