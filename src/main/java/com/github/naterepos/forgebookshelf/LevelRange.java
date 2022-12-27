package com.github.naterepos.forgebookshelf;

import com.github.naterepos.forgebookshelf.utility.RandomUtilities;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class LevelRange {
    private int min;
    private int max;

    @Internal public LevelRange() {}

    public LevelRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getRandomLevel() {
        return RandomUtilities.getNumberBetweenInclusive(getMin(), getMax());
    }
}