package org.blacksun.utils;

import java.util.Random;

public final class RandomGenerator {
    private final Random random;
    private int upperBound;
    private int lowerBound;

    public RandomGenerator(int upperBound) {
        this(upperBound, 0);
    }

    public RandomGenerator(int upperBound, int lowerBound) {
        random = new Random();
        this.upperBound = upperBound;
        this.lowerBound = lowerBound;
    }

    public void incUpperBound() {
        setUpperBound(upperBound + 1);
    }

    public int getUpperBound() {
        return upperBound;
    }

    public void setUpperBound(int upperBound) {
        if (upperBound < lowerBound)
            throw new IllegalArgumentException("Upper bound is less than lower");
        this.upperBound = upperBound;
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public void setLowerBound(int lowerBound) {
        if (lowerBound > upperBound)
            throw new IllegalArgumentException("Lower bound is greater than upper");
        this.lowerBound = lowerBound;
    }

    public int next() {
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    public int nextExcept(int except) {
        int result = next();
        while (result == except) {
            result = next();
        }
        return result;
    }
}
