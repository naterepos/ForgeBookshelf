package com.github.naterepos.forgebookshelf.utility;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandomUtilities {

    private static final Random RAND = new Random();

    public static <T> T getRandomElementFromCollection(Collection<T> collection) {
        int index = RAND.nextInt(collection.size());
        Iterator<T> iterator = collection.iterator();

        for(int i = 0; i < index; ++i) {
            iterator.next();
        }

        return iterator.next();
    }

    public static int getNumberBetweenInclusive(int min, int max) {
        if(min == max) return min;
        int difference = Math.abs(max - min);
        int rand = RAND.nextInt(difference + 1);
        return min < max ? min + rand : min - rand;
    }
}
