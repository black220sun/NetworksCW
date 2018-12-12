package common

import java.util.Objects

class Pair<T, R>(val first: T, val second: R) {

    override fun equals(o: Any?): Boolean {
        if (this === o)
            return true
        if (o == null || javaClass != o.javaClass)
            return false
        val pair = o as Pair<*, *>?
        return first == pair!!.first && second == pair.second
    }

    override fun hashCode(): Int {
        return Objects.hash(first, second)
    }

    override fun toString(): String {
        return "Pair<$first, $second>"
    }
}
