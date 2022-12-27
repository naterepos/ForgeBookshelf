package com.github.naterepos.forgebookshelf.utility;

import com.github.naterepos.forgebookshelf.SimpleTime;
import com.github.naterepos.forgebookshelf.Task;

import java.util.concurrent.TimeUnit;

public class TaskUtilities {

    public static Task delayed(SimpleTime time, Runnable runnable) {
        return Task.builder().delay((long) ((double) time.toUnit(TimeUnit.MILLISECONDS) / 0.05)).execute(runnable).build();
    }

    public static Task delayed(Runnable runnable) {
        return Task.builder().delay(5L).execute(runnable).build();
    }

    public static Task repeated(SimpleTime interval, Runnable runnable) {
        return Task.builder().infinite().interval((long) ((double) interval.toUnit(TimeUnit.MILLISECONDS) / 50)).execute(runnable).build();
    }

    public static Task now(Runnable runnable) {
        return Task.builder().delay(1).execute(runnable).build();
    }
}
