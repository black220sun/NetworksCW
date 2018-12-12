package common

import java.util.ArrayList

class WeightList(vararg weights: Int) {
    private val weights = ArrayList<Int>()
    private val random: Randomer

    val weight: Int
        get() = weights[random.next()]

    init {
        for (weight in weights) {
            this.weights.add(weight)
        }
        random = Randomer(weights.size)
    }
}
