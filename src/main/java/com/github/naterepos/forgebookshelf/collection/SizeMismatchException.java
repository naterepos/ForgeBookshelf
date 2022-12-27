package com.github.naterepos.forgebookshelf.collection;

/**
 * A SizeMismatchException can occur when two collection inputs are given and a size condition such as one being too
 * small when compared to the other occurs.
 **/
public class SizeMismatchException extends RuntimeException {

    public SizeMismatchException(int givenSize, int requiredSize) {
        super("Two collections were given, but one collection did not meet the required size {\n" +
                "Given Size: " + givenSize + "\n" +
                "Required Size: " + requiredSize + "\n}\n");
    }
}