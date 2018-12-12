package utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public final class WeightList {
    private final ArrayList<Integer> weights;
    private final IntGenerator random;

    public WeightList(@NotNull int... weights) {
        this.weights = new ArrayList<>();
        for (int weight : weights) {
            this.weights.add(weight);
        }
        random = new IntGenerator(weights.length);
    }

    public int getWeight() {
        return weights.get(random.next());
    }
}
