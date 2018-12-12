package mvc.model.channel

import mvc.model.node.Node
import common.Selectable
import common.ToInfo

interface Channel : ToInfo, Selectable {

    val fromNode: Node

    val toNode: Node

    var weight: Int

    var errors: Double

    var isUsed: Boolean

    fun connect()

    fun remove()

    override fun toInfo(): String {
        return toString()
    }

    companion object {
        const val ERRORS_AMOUNT = 0.1
    }
}
