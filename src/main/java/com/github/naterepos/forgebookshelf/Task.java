package com.github.naterepos.forgebookshelf;


import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nonnull;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Task {

    private Consumer<Task> consumer;

    private long interval;
    private long currentIteration;
    private final long iterations;

    private long ticksRemaining;
    private boolean expired;

    Task(Consumer<Task> consumer, long delay, long interval, long iterations) {
        this.consumer = consumer;
        this.interval = interval;
        this.iterations = iterations;

        if (delay > 0) {
            ticksRemaining = delay;
        }
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired() {
        expired = true;
    }

    public void restartWithInterval(SimpleTime modified) {
        interval = (long) ((double) modified.toUnit(TimeUnit.MILLISECONDS) / 50);
        currentIteration = 0;
        ticksRemaining = 0;
    }

    public void restartWithAction(Runnable action) {
        consumer = (task) -> action.run();
    }

    void tick() {
        if (!expired) {
            this.ticksRemaining = Math.max(0, --ticksRemaining);

            if (ticksRemaining == 0) {
                consumer.accept(this);
                currentIteration++;

                if (interval > 0 && (currentIteration < iterations || iterations == -1)) {
                    ticksRemaining = interval;
                } else {
                    expired = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tick();
            if (isExpired()) {
                MinecraftForge.EVENT_BUS.unregister(this);
            }
        }
    }

    public static TaskBuilder builder() {
        return new TaskBuilder();
    }

    public static class TaskBuilder {

        private Consumer<Task> consumer;
        private long delay;
        private long interval;
        private long iterations = 1;

        public TaskBuilder execute(@Nonnull Runnable runnable) {
            this.consumer = (task) -> runnable.run();
            return this;
        }

        public TaskBuilder execute(@Nonnull Consumer<Task> consumer) {
            this.consumer = consumer;
            return this;
        }

        public TaskBuilder delay(long delay) {
            if (delay < 0) {
                throw new IllegalArgumentException("delay must not be below 0");
            }
            this.delay = delay;
            return this;
        }

        public TaskBuilder interval(long interval) {
            if (interval < 0) {
                throw new IllegalArgumentException("interval must not be below 0");
            }
            this.interval = interval;
            return this;
        }

        public TaskBuilder iterations(long iterations) {
            if (iterations < -1) {
                throw new IllegalArgumentException("iterations must not be below -1");
            }
            this.iterations = iterations;
            return this;
        }

        public TaskBuilder infinite() {
            return iterations(-1);
        }

        public Task build() {
            if (consumer == null) {
                throw new IllegalStateException("consumer must be set");
            }
            return new Task(consumer, delay, interval, iterations);
        }

    }
}