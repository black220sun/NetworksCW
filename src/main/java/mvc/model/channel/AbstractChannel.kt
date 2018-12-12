package mvc.model.channel

import mvc.model.node.Node

import java.util.Objects

abstract class AbstractChannel(override val fromNode: Node, override val toNode: Node, override var weight: Int, override var errors: Double) : Channel {
    override var isUsed = false
    override var isSelected = false

    protected abstract val direction: String

    override fun connect() {
        fromNode.addConnection(this)
    }

    override fun remove() {
        fromNode.removeConnection(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as AbstractChannel?
        return isUsed == that!!.isUsed &&
                weight == that.weight &&
                java.lang.Double.compare(errors, that.errors) == 0 &&
                fromNode == that.fromNode &&
                toNode == that.toNode
    }

    override fun hashCode(): Int {
        return Objects.hash(fromNode, toNode, isUsed, weight, errors)
    }

    override fun toString(): String {
        var busy = ""
        if (isUsed) {
            busy = ", busy"
        }
        return String.format("%s %s %s (weight=%d, errors=%.1f%%%s)",
                fromNode, direction, toNode, weight, errors * 100, busy)
    }
}
