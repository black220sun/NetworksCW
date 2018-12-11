package org.blacksun.utils

import java.util.Random

class RandomGenerator @JvmOverloads constructor(private val upperBound: Int, private val lowerBound: Int = 0) {
    private val random: Random = Random()

    operator fun next(): Int {
        return random.nextInt(upperBound - lowerBound) + lowerBound
    }

    fun nextExcept(except: Int): Int {
        var result = next()
        while (result == except) {
            result = next()
        }
        return result
    }
}
