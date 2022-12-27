package com.github.naterepos.forgebookshelf.collection;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class designed to retrieve random elements with weighted chances accounted for.
 */
public class Randomizer {

    private final Random generator;

    public Randomizer() {
        this.generator = new Random();
    }

    public Randomizer(long seed) {
        this.generator = new Random(seed);
    }

    /**
     * Gets a map of weighted values with each element having the {@link Weighted} quality
     * @param weightedOptions an un-randomized list of options
     * @param possibilities a set of unique keys that will be used in the final product
     * @param allowDuplicateValues if the algorithm should allow duplicates in the final product
     * @param <Element> type of element (often the active object)
     * @param <Key> type of key (often numbers but can have other unique IDs)
     * @return a randomized map
     */
    public <Element extends Weighted, Key> Map<Key, Element> getRandomElementsWithWeights(List<Element> weightedOptions, Set<Key> possibilities, boolean allowDuplicateValues) {
        try {
            return getRandomElementsWithWeights(weightedOptions.stream().collect(Collectors.toMap(Function.identity(), Element::getWeight)), possibilities, allowDuplicateValues);
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Gets a list of weighted values with each element having the {@link Weighted} quality
     * @param weightedOptions an un-randomized list of options
     * @param amount the number of elements to pick
     * @param allowDuplicateValues if the algorithm should allow duplicates in the final product
     * @param <Element> type of element (often the active object)
     * @return a randomized list
     */
    public <Element extends Weighted> List<Element> getRandomElementsWithWeights(Set<Element> weightedOptions, int amount, boolean allowDuplicateValues) {
        Set<Integer> keys = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            keys.add(i);
        }
        try {
            return new ArrayList<>(getRandomElementsWithWeights(weightedOptions.stream().collect(Collectors.toMap(Function.identity(), Element::getWeight)), keys, allowDuplicateValues).values());
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Gets a list of weighted values with the weight being provided as the value in the weightedOptions map
     * @param weightedOptions an un-randomized map of options with weights
     * @param amount the number of elements to pick
     * @param allowDuplicateValues if the algorithm should allow duplicates in the final product
     * @param <Element> type of element (often the active object)
     * @return a randomized list
     */
    public <Element> List<Element> getRandomElementsWithWeights(Map<Element, Integer> weightedOptions, int amount, boolean allowDuplicateValues) {
        Set<Integer> keys = new HashSet<>();
        for (int i = 0; i < amount; i++) {
            keys.add(i);
        }
        try {
            return new ArrayList<>(getRandomElementsWithWeights(weightedOptions, keys, allowDuplicateValues).values());
        } catch (SizeMismatchException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Gets a map of weighted values with the weight being provided as the value in the weightedOptions map
     * @param weightedOptions an un-randomized map of options with the value being the weight
     * @param possibilities a list of unique keys that will be used in the final product
     * @param allowDuplicateValues if the algorithm should allow duplicates in the final product
     * @param <Element> type of element (often the active object)
     * @param <Key> type of key (often numbers but can have other unique IDs)
     * @throws SizeMismatchException thrown if weightedOptions is smaller than possibilities
     *
     * @return a randomized map
     */
    public <Element, Key> Map<Key, Element> getRandomElementsWithWeights(Map<Element, Integer> weightedOptions, Set<Key> possibilities, boolean allowDuplicateValues) throws SizeMismatchException {
        if(weightedOptions.size() < possibilities.size()) {
            throw new SizeMismatchException(weightedOptions.size(), possibilities.size());
        }
        AtomicReference<Integer> max = new AtomicReference<>(0);
        Map<Element, Integer> weights = new HashMap<>();
        Map<Key, Element> actual = new HashMap<>();

        for (Map.Entry<Element, Integer> item : weightedOptions.entrySet()) {
            weights.put(item.getKey(), item.getValue() + max.get());
            max.set(max.get() + item.getValue());
        }

        possibilities.forEach(slot -> {
            int random = generator.nextInt(max.get());
            Map.Entry<Element, Integer> weightedRandomOption = weights.entrySet().stream().sorted(Map.Entry.comparingByValue()).filter(entry -> random <= entry.getValue()).findFirst()
                    .orElse(weights.entrySet().stream().min(Map.Entry.comparingByValue()).orElse(null));
            actual.put(slot, Objects.requireNonNull(weightedRandomOption).getKey());

            if (!allowDuplicateValues) {
                weights.remove(weightedRandomOption.getKey());
                max.set(max.get() - weightedOptions.get(weightedRandomOption.getKey()));
            }
        });
        return actual;
    }
}