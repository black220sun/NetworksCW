package org.blacksun.graph.channel

import org.blacksun.graph.node.GraphNode
import org.blacksun.utils.Selectable
import org.blacksun.utils.StringRepresentable

/**
 * Represents connection between two nodes from one network.
 * <br></br>
 * Parameters of channel are mutable but connected nodes could not be changed.
 */
interface Channel : StringRepresentable, Selectable {

    val fromNode: GraphNode

    val toNode: GraphNode

    var weight: Int

    var errors: Double

    var isUsed: Boolean

    fun connect()

    fun remove()

    override fun stringRepresentation(): String {
        return toString()
    }

    companion object {
        const val ERRORS_AMOUNT = 0.1
    }
}
