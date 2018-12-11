package org.blacksun.utils

import java.util.ArrayList

class WeightList(vararg weights: Int) {
    private val weights = ArrayList<Int>()
    private val random: RandomGenerator

    val weight: Int
        get() = weights[random.next()]

    init {
        for (weight in weights) {
            this.weights.add(weight)
        }
        random = RandomGenerator(weights.size)
    }
}
